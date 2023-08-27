package com.diamondhunter.util.sound;

import com.diamondhunter.hub.DiamondHunter;
import com.diamondhunter.util.logging.Logger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * A {@code SimpleSoundPlayer} is a manageable audio-player which is used
 * to play sounds with a certain file format. Because {@code SimpleSoundPlayer}
 * does not support all audio file formats, use of this class may not be ideal
 * if the given audio format is not well-known (or an MP3 audio file). To find
 * supported audio file formats, visit {@link AudioFileFormat.Type}. All currently
 * supported file types are:
 *
 * <blockquote><pre>WAVE (*.wav), AU (*.au), AIFF (*.aiff), AIF-C (*.aifc), SND (*.snd)</pre></blockquote>
 * <p>
 * {@code SimpleSoundPlayer}s are typically used for simple audio use (as suggested by
 * the name) such as playing one sound. Although, if you play multiple sounds using this
 * class, such a choice my increase CPU usage and hog thread usage. To evade such a situation,
 * using {@link ComplexSoundPlayer} is recommended to play multiple sounds of which are long
 * or are heavily repeated. A {@code ComplexSoundPlayer} is used to put all of the sounds
 * on a different {@code Thread} (defined by the {@link SoundThread} class which can be used to
 * add sounds to a list of playable {@link ComplexSoundPlayer}s). Although, a
 * {@link ComplexSoundPlayer} uses a {@code SimpleSoundPlayer} to play sounds. The only difference
 * is that it plays it on a different thread
 * <p>
 * Even though, a {@link ComplexSoundPlayer} does not have as much accessibility as a
 * {@code SimpleSoundPlayer}, it can still be useful in tight situations.
 *
 * @see ComplexSoundPlayer
 * @see SoundThread
 */
public class SimpleSoundPlayer extends SoundPlayer {
    /**
     * {@inheritDoc}
     *
     * @param url url The {@code URL} to read from
     */
    public SimpleSoundPlayer(URL url) {
        super(url);
    }

    /**
     * {@inheritDoc}
     *
     * @param uri url The {@code URI} to read from
     */
    public SimpleSoundPlayer(URI uri) {
        super(uri);
    }

    /**
     * {@inheritDoc}
     *
     * @param file url The {@code File} to read from
     */
    public SimpleSoundPlayer(File file) {
        super(file);
    }

    /**
     * Constructs a {@code SimpleSoundPlayer} with the given sound from the
     * {@code ComplexSoundPlayer}
     *
     * @param soundPlayer The {@code ComplexSoundPlayer} to read from
     */
    public SimpleSoundPlayer(ComplexSoundPlayer soundPlayer) {
        super(soundPlayer.inputStream);

        this.clip = soundPlayer.clip;
        this.inputStream = soundPlayer.inputStream;
        this.audioInputStream = soundPlayer.audioInputStream;
        this.microPausePosition = soundPlayer.microPausePosition;
        this.framePausePosition = soundPlayer.framePausePosition;
    }

    /**
     * Plays the sound that is is assigned to this {@code SoundPlayer}.
     * If the given sound was previously paused (by an invocation of the
     * {@link #pauseSound()}} or {@link #pauseSound(long)} method), it will
     * resume the sound at the desired position specified by those methods.
     *
     * @see #pauseSound()
     * @see #pauseSound(long)
     */
    @Override
    public synchronized void playSound() {
        if (isFinishedPlayback()) return;

        if (microPausePosition == -1)
            playSound(framePausePosition == -1 ? 0 : framePausePosition);
        else
            playSound(microPausePosition);
    }

    /**
     * Plays the sound that is is assigned to this {@code SoundPlayer} at
     * the given position in microseconds.
     *
     * @param microPosition The position in microseconds to start at
     * @see #playSound()
     */
    public synchronized void playSound(long microPosition) {
        if (isFinishedPlayback()) return;

        try {
            if (!clip.isOpen())
                clip.open(audioInputStream);
            clip.setMicrosecondPosition(microPosition);

            clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

        paused = false;
    }

    /**
     * Plays the sound that is is assigned to this {@code SoundPlayer} at
     * the given frame position.
     *
     * @param framePosition The frame position to start at
     */
    public synchronized void playSound(int framePosition) {
        if (isFinishedPlayback()) return;

        try {
            if (!clip.isOpen())
                clip.open(audioInputStream);
            clip.setFramePosition(framePosition);
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            DiamondHunter.getImplementation().getLogger().logLine(Logger.Level.WARNING, "Could not open sound: " + audioInputStream);
        }

        paused = false;
    }

    /**
     * Stops the currently playing sound if a sound is currently playing.
     *
     * @see #playSound()
     */
    @Override
    public synchronized void stopSound() {
        clip.flush();
        clip.stop();
        paused = false;
    }

    /**
     * Pauses the current sound if it is playing and resumes it at the
     * position it was paused at (by an invocation of the
     * {@link #playSound()} method).
     *
     * @see #pauseSound()
     * @see #pauseSound(long)
     */
    @Override
    public synchronized void pauseSound() {
        pauseSound(getMicrosecondPosition() + 10); // 10 = added for pause effect (so it doesn't play the same frame twice)
    }

    /**
     * Pauses the current sound if it is playing and once this sound is
     * resumed (by an invocation of the {@link #playSound()} method),
     * it will resume at the given position.
     *
     * @param resumeMicroPosition The position at which we should resume the sound
     * @see #playSound()
     * @see #pauseSound()
     */
    public synchronized void pauseSound(long resumeMicroPosition) {
        microPausePosition = resumeMicroPosition;
        stopSound();
        paused = true;
    }

    /**
     * Pauses the current sound if it is playing and once this sound is
     * resumed (by an invocation of the {@link #playSound()} method),
     * it will resume at the given position.
     *
     * @param resumeFramePosition The position at which we should resume the sound
     * @see #playSound()
     * @see #pauseSound()
     */
    public synchronized void pauseSound(int resumeFramePosition) {
        framePausePosition = resumeFramePosition;
        stopSound();
    }

    @Override
    public String toString() {
        return "[inputStream=" + inputStream + ";audioInputStream=" + audioInputStream + ";playing=" + isPlaying()
                + ";paused=" + paused + ";finishedPlayback=" + isFinishedPlayback() + "]";
    }
}
