package Util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public final class Sound {
    private Sound() {}

    // plays the sound once (the clip is closed when it ends) or in a loop; returns null on error
    public static Clip play(String soundFile, float volume, boolean loop) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
            }

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            }
            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.err.println("Error playing sound " + soundFile + ": " + e.getMessage());
            return null;
        }
    }

    public static void stop(Clip clip) {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
