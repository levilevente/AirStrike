import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Missile.*;
import Panels.*;
import Plane.*;
import ShipPackage.*;
import Strategy.*;
import Timer.*;

public class AirstrikeFrame extends JFrame {

    private List<Ship> ships;
    private Sea sea;
    private Plane plane;
    private List <Missile> missiles;
    private ShipController[] shipController;
    private PlaneController planeController;
    private MissileController missileController;
    private JButton startButton;
    private JButton optionsButton;
    private TimerController timerControl;
    private TimerModel timerModel;
    private int timeForRound;
    private static Clip clip;           //for music (changes between panels)
    private String playerName;

    public AirstrikeFrame(){

        this.timeForRound = 40;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(100,100,640,640);
        this.setTitle("Airstrike");
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        addSong("Sound/MenuSong.wav");


        plane = new PlaneModel(320,320,35,45);
        missiles = new ArrayList<Missile>(3);
        ships = new ArrayList<>();
        //adding the first ships to panel
        ships.add(new RandomShipGenerator(3).getRandomShip());
        ships.add(new RandomShipGenerator(4).getRandomShip());
        ships.add(new RandomShipGenerator(5).getRandomShip());
        ships.add(new RandomShipGenerator(1).getRandomShip());
        ships.add(new RandomShipGenerator(2).getRandomShip());
        ships.add(new RandomShipGenerator(6).getRandomShip());
        timerModel = new TimerModel(timeForRound, 0);

        Thread[] missilesThread = new Thread[3];
        Thread[] shipthreads = new Thread[6];

        int threadIndex = 0;
        shipController = new ShipController[6];
        sea = new Sea(ships, plane, missiles, timerModel);
        for (Ship i : ships){
            shipController[threadIndex] = new ShipController(i,sea);
            shipthreads[threadIndex] = new Thread(shipController[threadIndex]);
            shipthreads[threadIndex].start();
            threadIndex++;
        }



        CardLayout layout = new CardLayout();
        JPanel cardPanel = new JPanel(); //fontos
        cardPanel.setLayout(layout);
        this.add(cardPanel);


        // Creating a panel for menu
        MenuPanel menuPanel = new MenuPanel();
        menuPanel.setLayout(new GridLayout(4,1));
        JButton playButton = makeButton("Images/Buttons/PlayButton.png", 400, 130);     //play button
        JButton optionsButton = makeButton("Images/Buttons/OptionsButton.png", 400, 130);   //OptionsSettings button (set time for game and name of the player)
        JButton leaderboardButton = makeButton("Images/Buttons/LeaderboardButton.png", 400, 130);   //shows the saved players and their scores
        JButton controlsButton = makeButton("Images/Buttons/ControlsButton.png", 400, 130);   //shows the saved players and their scores
        menuPanel.add(playButton);
        menuPanel.add(leaderboardButton);
        menuPanel.add(optionsButton);
        menuPanel.add(controlsButton);



        //Creating panel for options
        OptionsPanel optionsPanel = new OptionsPanel();
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20,300));
        JTextArea textArea = new JTextArea("Name TimeForRound (min 40) ex: Levi 40");           //text area for getting the name and the time from the player
        textArea.setPreferredSize(new Dimension(370,30));
        textArea.setFont(new Font("Times New Roman", Font.PLAIN,20));
        optionsPanel.add(textArea);
        JButton submitButton= makeButton("Images/Buttons/SaveButton.png", 100, 50);    //adding Submit/Save button
        JButton backButton= makeButton("Images/Buttons/BackButton.png", 100, 50);       //adding a back button wich drop us back to menu panel
        optionsPanel.add(submitButton);
        optionsPanel.add(backButton);




        //Creating panel for leaderboard
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel();
        leaderboardPanel.setLayout(new BorderLayout());
        JButton viewedBoard = makeButton("Images/Buttons/BackButton.png", 200, 100);     //get us back to menu panel
        leaderboardPanel.add(viewedBoard, BorderLayout.SOUTH);



        //Creating panel for controls
        ControlsPanel controlsPanel = new ControlsPanel();
        controlsPanel.setLayout(new BorderLayout());
        JButton backToMenu = makeButton("Images/Buttons/BackButton.png", 100, 50);
        controlsPanel.add(backToMenu, BorderLayout.SOUTH);



        // adding the panels to the cardPanel
        cardPanel.add(menuPanel, "menuPanel");
        cardPanel.add(sea, "sea");
        cardPanel.add(optionsPanel, "optionsPanel");
        cardPanel.add(leaderboardPanel, "leaderboardPanel");
        cardPanel.add(controlsPanel, "controlsPanel");



        //play button:
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.show(cardPanel, "sea");          //shows the sea
                planeController = new PlaneController(plane, sea);  //starts the plane
                Thread planeThread = new Thread(planeController);
                planeThread.start();


                //Creating timer for the game
                TimerView timerView = new TimerView(timerModel);        //starts the timer
                timerControl = new TimerController(timerModel,timerView);
                Thread threadTimer = new Thread(timerControl);
                threadTimer.start();
                timerView.setPreferredSize(new Dimension(20,40));

                stopSound();                //plays other song
                addSong("Sound/BattleSong.wav");
            }
        });
        //leaderboard button:
        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.show(cardPanel, "leaderboardPanel");     //show the leaderboard with the back button on it
                Java8Strategy java8Strategy = new Java8Strategy();
                List<Players> info = java8Strategy.processFile("leaderboard.txt");
                StringBuilder stringInfo = new StringBuilder();
                for (Players p : info) {
                    stringInfo.append(p.toString()).append('\n');
                }
                JTextArea finalText = new JTextArea(stringInfo.toString());

                finalText.setOpaque(false);                // set JTextArea background to be transparent


                finalText.setForeground(Color.BLACK);                // set JTextArea font color (foreground) to be readable
                finalText.setFont(new Font("Times New Roman", Font.BOLD, 20));

                JScrollPane scrollPane = new JScrollPane(finalText);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setBounds(100,100,100,100);
                leaderboardPanel.add(scrollPane, BorderLayout.CENTER);                // add the JTextArea to the leaderboardPanel

                //when the button is pressed the menu panel will be displayed
                viewedBoard.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        layout.show(cardPanel, "menuPanel");
                    }
                });

            }
        });
        //options button:
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.show(cardPanel, "optionsPanel");     //options panel displayed
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //if the submit button is pressed the information is being collected
                        String text = textArea.getText();
                        String [] tokens = text.split(" ");
                        setPlayerName(tokens[0]);
                        int chosedTime = Integer.parseInt(tokens[1]);
                        if (chosedTime < 40) {      //the time for the game needs to be min 40
                            setPlayerName("Player"+chosedTime);
                            setTimeForRound(40);
                        }else{
                            setTimeForRound(chosedTime);
                        }
                    }
                });

                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        layout.show(cardPanel,"menuPanel");
                    }   //back to menu
                });
            }
        });
        //controls button:
        controlsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.show(cardPanel, "controlsPanel");
                backToMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        layout.show(cardPanel, "menuPanel");
                    }
                });
            }
        });


        cardPanel.setFocusable(true);       //adding key listeners to game card panel needs to be in focus
        cardPanel.requestFocusInWindow();
        cardPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e){}

            @Override
            public void keyPressed(KeyEvent e) {
                BufferedImage pi;
                try {
                    pi = ImageIO.read(new File("Images/Plane.png"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                switch (e.getKeyChar()){
                    //depending on wich button is pressed the direction and image direction are saved again
                    case 'w':
                    case 'W':
                        plane.setDirection(0);
                        plane.setImage(rotateImage(pi, 0));
                        break;
                    case 's':
                    case 'S':
                        plane.setDirection(2);
                        plane.setImage(rotateImage(pi, 180));
                        break;
                    case 'a':
                    case 'A':
                        plane.setDirection(3);
                        plane.setImage(rotateImage(pi, 270));
                        break;
                    case 'd':
                    case 'D':
                        plane.setDirection(1);
                        plane.setImage(rotateImage(pi, 90));
                        break;
                    case ' ':       // 'Space' key means the plane drops a bomb
                        if (sea.activeMissiles() < 3){      // only 3 bombs can be 'active' (dropped)
                            //setting the missile coordinates
                            Missile newMissile = new MissileModel(plane.getX() + plane.getWidth() / 2 - 3 , plane.getY() + plane.getHeight()/2 - 2, 5, 9, 5);
                            try {
                                BufferedImage mi = ImageIO.read(new File("Images/Missile.png"));
                                newMissile.setImage(rotateImage(mi, plane.getDirection() * 90));    //rotating the image as well as the plane image
                                newMissile.setHeight(9);
                                newMissile.setWidth(7);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            if (missiles.size() <= 2){  //  the first 3 missiles are added and (else) the other ones are modified
                                missiles.add(newMissile);
                                missileController = new MissileController(missiles.get(missiles.size() - 1), sea);
                                missilesThread[missiles.size() - 1] = new Thread(missileController);
                                missilesThread[missiles.size() - 1].start();
                            } else{
                                for (int i = 0; i < missiles.size(); i++){
                                    if (missiles.get(i).getActive() == 0){
                                        missiles.set(i, newMissile);
                                        missileController = new MissileController(missiles.get(i), sea);
                                        missilesThread[i] = new Thread(missileController);
                                        missilesThread[i].start();
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });


        this.setVisible(true);      // this later because the time delay


        while (timerModel.getStart() > timerModel.getStop()) {
            for (int i = 0; i < ships.size(); i++) {
                //checking if the ships needs to be replaced or not (when missile hits a ship the sip coordinates will be 640)
                if (ships.get(i).getY() <= (-1 * ships.get(i).getHeight())) {
                    int zone = ships.get(i).getZone();
                    ships.set(i, new RandomShipGenerator(zone).getRandomShip());
                    shipController[i] = new ShipController(ships.get(i),sea);
                    shipthreads[i] = new Thread(shipController[i]);
                    shipthreads[i].start();
                }
            }
        }

        //stop the moving
        plane.setMoving(false);
        for (Thread stopThread : shipthreads){
            if (stopThread.isAlive()){
                stopThread.stop();
            }
        }

        sea.setScoreShowing(true);

        String fileName = "leaderboard.txt";
        if (playerName == null){
            playerName = "Player";
        }

        int newScore = sea.getDestroyedShipScore();
        //update the leaderboards
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            List<String> lines = new ArrayList<>();

            String line;
            boolean playerExists = false;

            // read each line in the file
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" ");
                String existingPlayerName = parts[0];
                int existingScore = Integer.parseInt(parts[1]);

                // check if the player exists in the file
                if (existingPlayerName.equals(playerName)) {
                    playerExists = true;

                    // update the score if the new score is higher
                    if (newScore > existingScore) {
                        existingScore = newScore;
                    }
                }

                // add the existing or updated line to the list
                lines.add(existingPlayerName + " " + existingScore);
            }

            // if the player does not exist, add a new line
            if (!playerExists) {
                lines.add(playerName + " " + newScore);
            }

            // write the updated lines back to the file
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
                for (String updatedLine : lines) {
                    bufferedWriter.write(updatedLine);
                    bufferedWriter.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error updating the leaderboard: " + e.getMessage());
        }


        //adding the final panel with the score and a button (exit) on it

        FinalPanel finalPanel = new FinalPanel(sea.getDestroyedShipScore());
        cardPanel.add(finalPanel, "finalPanel");
        finalPanel.setLayout(new BorderLayout());
        JButton finalExit = makeButton("Images/Buttons/StopButton.png", 200,100);
        finalExit.setBounds(350,200, finalExit.getPreferredSize().width, finalExit.getPreferredSize().height);


        Java8Strategy java8Strategy = new Java8Strategy();
        List<Players> info = java8Strategy.processFile("leaderboard.txt");

        String finalStatistics = java8Strategy.printStatistics(info);        //everytime when the leaderboard button is pressed on the console will be displayed the statistics

        JTextArea finalText = new JTextArea(finalStatistics);

        finalText.setOpaque(false);                // set JTextArea background to be transparent


        finalText.setForeground(Color.BLACK);                // set JTextArea font color (foreground) to be readable
        finalText.setFont(new Font("Times New Roman", Font.BOLD, 20));

        JScrollPane scrollPane = new JScrollPane(finalText);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBounds(100,100,100,100);
        finalPanel.add(scrollPane, BorderLayout.SOUTH);                // add the JTextArea to the leaderboardPanel
        finalPanel.add(finalExit, BorderLayout.NORTH);

        layout.show(cardPanel, "finalPanel");
        finalExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }

    private static BufferedImage rotateImage(BufferedImage img, double angle) {

        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
    private static void playSound(String soundFile, float volume, boolean loop) {
        try {
            File file = new File(soundFile);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            gainControl.setValue(volume);
            if (loop){
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }else{
                clip.start();
            }

            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.out.println("Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
    private void addSong(String menuSong){
        playSound(menuSong, -15.0f, true);
    }
    private void setTimeForRound(int timeForRound){
        this.timeForRound = timeForRound;
        timerModel.setStart(timeForRound);
    }
    private void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    private JButton makeButton(String image, int width, int height){
        ImageIcon imageIcon = null;
        try{
            BufferedImage img = ImageIO.read(new File(image));
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);
        }catch (Exception e){
            e.getMessage();
        }
        JButton button = new JButton(imageIcon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }
}