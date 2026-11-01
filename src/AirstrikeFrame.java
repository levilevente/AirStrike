import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import Missile.*;
import Panels.*;
import Plane.*;
import ShipPackage.*;
import Strategy.*;
import Timer.*;
import Util.Images;
import Util.Sound;

public class AirstrikeFrame extends JFrame {

    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private static final String MENU_SONG = "Sound/MenuSong.wav";
    private static final String BATTLE_SONG = "Sound/BattleSong.wav";
    private static final float MUSIC_VOLUME = -15.0f;
    private static final Font TEXT_FONT = new Font("Times New Roman", Font.BOLD, 20);

    private static final int MIN_ROUND_TIME = 40;
    private static final int MAX_ACTIVE_MISSILES = 3;
    private static final int MISSILE_LIFETIME = 5;      // seconds: 3 seconds falling + 2 seconds explosion
    private static final int PLANE_WINGSPAN = 35;       // plane width when it flies up or down
    private static final int PLANE_LENGTH = 45;         // plane height when it flies up or down
    private static final int MISSILE_WIDTH = 7;
    private static final int MISSILE_LENGTH = 9;

    // names of the cards in the card layout
    private static final String MENU_CARD = "menuPanel";
    private static final String SEA_CARD = "sea";
    private static final String OPTIONS_CARD = "optionsPanel";
    private static final String LEADERBOARD_CARD = "leaderboardPanel";
    private static final String CONTROLS_CARD = "controlsPanel";
    private static final String FINAL_CARD = "finalPanel";

    private final List<Ship> ships = new CopyOnWriteArrayList<>();     // read by the missile and paint threads
    private final List<Missile> missiles = new ArrayList<>();          // only used on the Swing thread
    private final Plane plane;
    private final Sea sea;
    private final TimerModel timerModel;
    private final Strategy leaderboard = new Java8Strategy();

    private final CardLayout layout = new CardLayout();
    private final JPanel cardPanel = new JPanel(layout);
    private final JTextArea leaderboardText = makeTextArea("");

    // images for each direction (index = Plane.UP, RIGHT, DOWN, LEFT)
    private final BufferedImage[] planeImages = rotations(Images.load("Images/Plane.png"));
    private final BufferedImage[] missileImages = rotations(Images.load("Images/Missile.png"));

    private volatile boolean gameRunning = false;
    private Clip music;                 //for music (changes between panels)
    private String playerName = "Player";

    public AirstrikeFrame(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(100,100,Sea.SIZE,Sea.SIZE);
        this.setTitle("Airstrike");
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        music = Sound.play(MENU_SONG, MUSIC_VOLUME, true);

        plane = new PlaneModel(320,320,PLANE_WINGSPAN,PLANE_LENGTH);
        timerModel = new TimerModel(MIN_ROUND_TIME, 0);
        sea = new Sea(ships, plane, missiles, timerModel);

        //adding the first ships, one in each zone
        for (int zone = 1; zone <= RandomShipGenerator.ZONE_COUNT; zone++){
            ships.add(startShip(new RandomShipGenerator(zone).getRandomShip()));
        }

        cardPanel.add(createMenuPanel(), MENU_CARD);
        cardPanel.add(sea, SEA_CARD);
        cardPanel.add(createOptionsPanel(), OPTIONS_CARD);
        cardPanel.add(createLeaderboardPanel(), LEADERBOARD_CARD);
        cardPanel.add(createControlsPanel(), CONTROLS_CARD);
        this.add(cardPanel);

        cardPanel.setFocusable(true);       //the key listener only works when the card panel is in focus
        cardPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e.getKeyChar());
            }
        });

        this.setVisible(true);
        cardPanel.requestFocusInWindow();
    }

    // runs until the round time is over, then shows the final panel
    public void runGame(){
        while (timerModel.getStart() > timerModel.getStop()) {
            //replace the ships that left the map or were destroyed with a new ship in the same zone
            for (int i = 0; i < ships.size(); i++) {
                Ship ship = ships.get(i);
                if (ship.isGone()) {
                    ships.set(i, startShip(new RandomShipGenerator(ship.getZone()).getRandomShip()));
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        endGame();
    }

    private Ship startShip(Ship ship){
        new Thread(new ShipController(ship, sea)).start();
        return ship;
    }

    private JPanel createMenuPanel(){
        MenuPanel menuPanel = new MenuPanel();
        menuPanel.setLayout(new GridLayout(4,1));
        JButton playButton = makeButton("Images/Buttons/PlayButton.png", 400, 130);
        JButton leaderboardButton = makeButton("Images/Buttons/LeaderboardButton.png", 400, 130);   //shows the saved players and their scores
        JButton optionsButton = makeButton("Images/Buttons/OptionsButton.png", 400, 130);   //set the name of the player and the time for the game
        JButton controlsButton = makeButton("Images/Buttons/ControlsButton.png", 400, 130);   //shows the keys
        menuPanel.add(playButton);
        menuPanel.add(leaderboardButton);
        menuPanel.add(optionsButton);
        menuPanel.add(controlsButton);

        playButton.addActionListener(e -> startGame());
        leaderboardButton.addActionListener(e -> showLeaderboard());
        optionsButton.addActionListener(e -> layout.show(cardPanel, OPTIONS_CARD));
        controlsButton.addActionListener(e -> layout.show(cardPanel, CONTROLS_CARD));
        return menuPanel;
    }

    private JPanel createOptionsPanel(){
        OptionsPanel optionsPanel = new OptionsPanel();
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20,300));
        JTextArea textArea = new JTextArea("Name TimeForRound (min 40) ex: Levi 40");    //the player writes the name and the time here
        textArea.setPreferredSize(new Dimension(370,30));
        textArea.setFont(new Font("Times New Roman", Font.PLAIN,20));
        JButton submitButton = makeButton("Images/Buttons/SaveButton.png", 100, 50);
        JButton backButton = makeButton("Images/Buttons/BackButton.png", 100, 50);
        optionsPanel.add(textArea);
        optionsPanel.add(submitButton);
        optionsPanel.add(backButton);

        submitButton.addActionListener(e -> saveOptions(textArea.getText()));
        backButton.addActionListener(e -> layout.show(cardPanel, MENU_CARD));
        return optionsPanel;
    }

    private JPanel createLeaderboardPanel(){
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel();
        leaderboardPanel.setLayout(new BorderLayout());
        JButton backButton = makeButton("Images/Buttons/BackButton.png", 200, 100);
        leaderboardPanel.add(makeScrollPane(leaderboardText), BorderLayout.CENTER);
        leaderboardPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> layout.show(cardPanel, MENU_CARD));
        return leaderboardPanel;
    }

    private JPanel createControlsPanel(){
        ControlsPanel controlsPanel = new ControlsPanel();
        controlsPanel.setLayout(new BorderLayout());
        JButton backButton = makeButton("Images/Buttons/BackButton.png", 100, 50);
        controlsPanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> layout.show(cardPanel, MENU_CARD));
        return controlsPanel;
    }

    private void startGame(){
        if (gameRunning){
            return;
        }
        gameRunning = true;
        layout.show(cardPanel, SEA_CARD);
        cardPanel.requestFocusInWindow();       //the clicked button has the focus, the keys must go to the card panel

        new Thread(new PlaneController(plane, sea)).start();
        new Thread(new TimerController(timerModel, new TimerView(timerModel))).start();

        Sound.stop(music);
        music = Sound.play(BATTLE_SONG, MUSIC_VOLUME, true);
    }

    private void showLeaderboard(){
        StringBuilder text = new StringBuilder();
        for (Players p : leaderboard.processFile(LEADERBOARD_FILE)) {
            text.append(p).append('\n');
        }
        leaderboardText.setText(text.toString());
        layout.show(cardPanel, LEADERBOARD_CARD);
    }

    // the text must be "Name Time", for example "Levi 40"
    private void saveOptions(String text){
        String[] tokens = text.trim().split("\\s+");
        int chosenTime;
        try {
            if (tokens.length != 2) {
                throw new NumberFormatException();
            }
            chosenTime = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Write your name and the time for the round, for example: Levi 40",
                    "Wrong format", JOptionPane.WARNING_MESSAGE);
            return;
        }
        playerName = tokens[0];
        timerModel.setStart(Math.max(chosenTime, MIN_ROUND_TIME));      //the time for the game needs to be min 40
        JOptionPane.showMessageDialog(this, "Saved: " + playerName + ", " + timerModel.getStart() + " seconds");
    }

    private void handleKey(char key){
        if (!gameRunning){
            return;
        }
        switch (Character.toLowerCase(key)){
            case 'w' -> turnPlane(Plane.UP);
            case 'd' -> turnPlane(Plane.RIGHT);
            case 's' -> turnPlane(Plane.DOWN);
            case 'a' -> turnPlane(Plane.LEFT);
            case ' ' -> dropMissile();      // 'Space' key means the plane drops a bomb
        }
    }

    private void turnPlane(int direction){
        // the plane is wider than high when it flies left or right; keep its center at the same place
        boolean horizontal = isHorizontal(direction);
        int width = horizontal ? PLANE_LENGTH : PLANE_WINGSPAN;
        int height = horizontal ? PLANE_WINGSPAN : PLANE_LENGTH;
        int centerX = plane.getX() + plane.getWidth() / 2;
        int centerY = plane.getY() + plane.getHeight() / 2;

        plane.setImage(planeImages[direction]);
        plane.setWidth(width);
        plane.setHeight(height);
        plane.setX(centerX - width / 2);
        plane.setY(centerY - height / 2);
        plane.setDirection(direction);
    }

    private void dropMissile(){
        if (sea.activeMissiles() >= MAX_ACTIVE_MISSILES){      // only 3 bombs can be 'active' (dropped)
            return;
        }
        int direction = plane.getDirection();
        boolean horizontal = isHorizontal(direction);
        int width = horizontal ? MISSILE_LENGTH : MISSILE_WIDTH;
        int height = horizontal ? MISSILE_WIDTH : MISSILE_LENGTH;
        //the bomb starts from the center of the plane
        int x = plane.getX() + plane.getWidth() / 2 - width / 2;
        int y = plane.getY() + plane.getHeight() / 2 - height / 2;

        Missile newMissile = new MissileModel(x, y, width, height, MISSILE_LIFETIME);
        newMissile.setImage(missileImages[direction]);

        //reuse the place of an old missile, so the list has max 3 missiles
        int freeIndex = -1;
        for (int i = 0; i < missiles.size(); i++){
            if (missiles.get(i).getActive() == 0){
                freeIndex = i;
                break;
            }
        }
        if (freeIndex >= 0){
            missiles.set(freeIndex, newMissile);
        } else {
            missiles.add(newMissile);
        }
        new Thread(new MissileController(newMissile, sea)).start();
    }

    private void endGame(){
        gameRunning = false;
        plane.setMoving(false);         //the ships stop by themselves when the time is over
        sea.setScoreShowing(true);

        int score = sea.getDestroyedShipScore();
        leaderboard.saveScore(LEADERBOARD_FILE, new Players(playerName, score));

        SwingUtilities.invokeLater(() -> showFinalPanel(score));
    }

    //the final panel shows the score, the statistics and an exit button
    private void showFinalPanel(int score){
        FinalPanel finalPanel = new FinalPanel(score);
        finalPanel.setLayout(new BorderLayout());
        JButton finalExit = makeButton("Images/Buttons/StopButton.png", 200,100);
        finalExit.addActionListener(e -> System.exit(0));

        String finalStatistics = leaderboard.printStatistics(leaderboard.processFile(LEADERBOARD_FILE));     //the statistics are also printed on the console
        finalPanel.add(makeScrollPane(makeTextArea(finalStatistics)), BorderLayout.SOUTH);
        finalPanel.add(finalExit, BorderLayout.NORTH);

        cardPanel.add(finalPanel, FINAL_CARD);
        layout.show(cardPanel, FINAL_CARD);
    }

    private static boolean isHorizontal(int direction){
        return direction == Plane.LEFT || direction == Plane.RIGHT;
    }

    private static BufferedImage[] rotations(BufferedImage image){
        BufferedImage[] rotated = new BufferedImage[4];
        if (image != null) {
            for (int direction = 0; direction < 4; direction++) {
                rotated[direction] = Images.rotate(image, direction * 90);
            }
        }
        return rotated;
    }

    // transparent, read-only text
    private static JTextArea makeTextArea(String text){
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setForeground(Color.BLACK);
        textArea.setFont(TEXT_FONT);
        return textArea;
    }

    private static JScrollPane makeScrollPane(JTextArea textArea){
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        return scrollPane;
    }

    private static JButton makeButton(String image, int width, int height){
        BufferedImage img = Images.load(image);
        JButton button = img != null
                ? new JButton(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH)))
                : new JButton(image);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }
}
