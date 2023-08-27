package com.diamondhunter.util.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public abstract class SoundPlayer {
    /**
     * The {@code AudioInputStream} for this {@code SoundPlayer}.
     */
    protected AudioInputStream audioInputStream;

    /**
     * The clip used to play the sound assigned to this {@code SoundPlayer}.
     */
    protected Clip clip;

    /**
     * The {@code InputStream} given from the assigned sound. This is used
     * to specify which sound should be played.
     */
    protected InputStream inputStream;

    /**
     * The position at which the sound from this {@code SoundPlayer} was
     * paused at in microseconds.
     *
     * @see #pauseSound()
     */
    protected long microPausePosition = -1;

    /**
     * The position at which the sound from this {@code SoundPlayer} was
     * paused at in frames.
     *
     * @see #pauseSound()
     */
    protected int framePausePosition = -1;

    /**
     * Whether this {@code SoundPlayer} has paused it's playback.
     *
     * @see #pauseSound()
     */
    protected boolean paused;

    /**
     * Constructs a {@code SoundPlayer} with the given sound defined as a
     * {@code URL}
     *
     * @param url The {@code URL} to read from
     */
    public SoundPlayer(URL url) {
        try {
            this.inputStream = url.openStream();
            audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a {@code SoundPlayer} with the given sound defined as a
     * {@code URI}
     *
     * @param uri The {@code URI} to read from
     */
    public SoundPlayer(URI uri) {
        try {
            this.inputStream = uri.toURL().openStream();
            audioInputStream = AudioSystem.getAudioInputStream(uri.toURL());
            clip = AudioSystem.getClip();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a {@code SoundPlayer} with the given sound defined as a
     * {@code File}
     *
     * @param file The {@code File} to read from
     */
    public SoundPlayer(File file) {
        this(file.toURI());
    }

    /**
     * Constructs a {@code SoundPlayer} with the given sound defined as an
     * {@code InputStream}
     *
     * @param in The {@code InputStream} to read from
     */
    public SoundPlayer(InputStream in) {
        try {
            this.inputStream = in;
            audioInputStream = AudioSystem.getAudioInputStream(in);
            clip = AudioSystem.getClip();
            stopSound();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound that is is assigned to this {@code SoundPlayer}.
     * If the given sound was previously paused (by an invocation of the
     * {@link #pauseSound()}}, it will resume the sound at the desired
     * position specified by those methods.
     *
     * Note that this method should be synchronized in case any thread issues
     * happen.
     */
    public abstract void playSound();

    /**
     * Pauses the current sound if it is playing and resumes it at the
     * position it was paused at (by an invocation of the
     * {@link #playSound()} method).
     *
     * Note that this method should be synchronized in case any thread issues
     * happen.
     *
     * @see #playSound()
     */
    public abstract void pauseSound();

    /**
     * Stops the currently playing sound if a sound is currently playing.
     *
     * Note that this method should be synchronized in case any thread issues
     * happen.
     *
     * @see #playSound()
     */
    public abstract void stopSound();

    /**
     * Sets the first and last sample frames that will be played in the loop.
     * The ending point must be greater than or equal to the starting point, and
     * both must fall within the size of the loaded media. A value of 0 for the
     * starting point means the beginning of the loaded media. Similarly, a
     * value of -1 for the ending point indicates the last frame of the media.
     *
     * @param start the loop's starting position, in sample frames (zero-based)
     * @param end   the loop's ending position, in sample frames (zero-based), or
     *              -1 to indicate the final frame
     * @throws IllegalArgumentException if the requested loop points cannot be
     *                                  set, usually because one or both falls outside the media's
     *                                  duration or because the ending point is before the starting point
     */
    public void setLoopingPoint(int start, int end) {
        clip.setLoopPoints(start, end);
    }

    /**
     * Sets whether this {@code SoundPlayer} should loop the sound it is or will be
     * playing. This can however still be achieved by using this {@link #loop(int)}
     * method which loops for a certain amount. Inserting {@link Clip#LOOP_CONTINUOUSLY}
     * as the {@code count} will continuously loop the desired song.
     *
     * @param looping Whether or not this {@code SoundPlayer} should loop the desired
     *                song.
     */
    public void setLooping(boolean looping) {
        if (looping)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else clip.loop(0);
    }

    /**
     * Loops the desired song for the specified amount.
     *
     * @param count The amount of times to loop
     */
    public void loop(int count) {
        clip.loop(count);
    }

    /**
     * Closes the line, indicating that any system resources in use by the line
     * can be released. If this operation succeeds, the line is marked closed
     * and a {@code CLOSE} event is dispatched to the line's listeners.
     *
     * @throws SecurityException if the line cannot be closed due to security
     *                           restrictions
     * @see LineEvent
     */
    public void close() {
        clip.close();
    }

    /**
     * Indicates whether this {@code SoundPlayer} is playing the desired sound.
     *
     * @return {@code true} if this {@code SoundPlayer} is actively capturing or rendering
     * sound, otherwise {@code false}
     */
    public boolean isPlaying() {
        return clip.isActive();
    }

    public boolean isPaused(){
        return paused;
    }

    public boolean isFinishedPlayback(){
        return !isPlaying() && !paused && microPausePosition != -1 && framePausePosition != -1;
    }

    /**
     * Returns the input stream for the given sound that this {@code SoundPlayer}
     * should play.
     *
     * @return Returns the {@code InputStream} for the sound of this {@code SoundPlayer}.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Returns the audio input stream for the given sound that is assign to this
     * {@code SoundPlayer}.
     *
     * @return Returns the {@code AudioInputStream} for this {@code SoundPlayer}
     */
    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    /**
     * Sets the media position in microseconds. When the clip begins playing the
     * next time, it will start at this position. The level of precision is not
     * guaranteed. For example, an implementation might calculate the
     * microsecond position from the current frame position and the audio sample
     * frame rate. The precision in microseconds would then be limited to the
     * number of microseconds per sample frame.
     * <p>
     * To obtain the current position in microseconds, use the
     * {@link #getMicrosecondPosition()} method.
     *
     * @param  microPosition the desired new media position, expressed in
     *         microseconds
     */
    public void setMicrosecondPosition(long microPosition){
        clip.setMicrosecondPosition(microPosition);
    }

    /**
     * Sets the media position in sample frames. The position is zero-based; the
     * first frame is frame number zero. When the clip begins playing the next
     * time, it will start by playing the frame at this position.
     * <p>
     * To obtain the current position in sample frames, use the
     * {@link #getFramePosition()} method.
     *
     * @param  framePosition the desired new media position, expressed in sample frames
     */
    public void setFramePosition(int framePosition){
        clip.setFramePosition(framePosition);
    }

    /**
     * Obtains the current position in the audio data, in sample frames. The
     * frame position measures the number of sample frames captured by, or
     * rendered from, the line since it was opened. This return value will wrap
     * around after 2^31 frames. It is recommended to use
     * {@code getLongFramePosition} instead.
     *
     * @return the number of frames already processed since the line was opened
     * @see #getLongFramePosition()
     */
    public int getFramePosition() {
        return clip.getFramePosition();
    }

    /**
     * Obtains the current position in the audio data, in sample frames. The
     * frame position measures the number of sample frames captured by, or
     * rendered from, the line since it was opened.
     *
     * @return the number of frames already processed since the line was opened
     */
    public long getLongFramePosition() {
        return clip.getLongFramePosition();
    }

    /**
     * Obtains the current position in the audio data, in microseconds. The
     * microsecond position measures the time corresponding to the number of
     * sample frames captured by, or rendered from, the line since it was
     * opened. The level of precision is not guaranteed. For example, an
     * implementation might calculate the microsecond position from the current
     * frame position and the audio sample frame rate. The precision in
     * microseconds would then be limited to the number of microseconds per
     * sample frame.
     *
     * @return the number of microseconds of data processed since the line was
     * opened
     */
    public long getMicrosecondPosition() {
        return clip.getMicrosecondPosition();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Clip){
            return clip.equals(obj);
        } else if(obj instanceof Number){
            return getMicrosecondPosition() == ((Number) obj).longValue();
        } else if(obj instanceof AudioInputStream){
            return audioInputStream.equals(obj);
        } else if(obj instanceof InputStream){
            return inputStream.equals(obj);
        } else if(obj instanceof SoundPlayer){
            return clip.equals(((SoundPlayer) obj).clip) && audioInputStream.equals(((SoundPlayer) obj).audioInputStream) && inputStream.equals(((SoundPlayer) obj).inputStream);
        }

        return super.equals(obj);
    }
}
