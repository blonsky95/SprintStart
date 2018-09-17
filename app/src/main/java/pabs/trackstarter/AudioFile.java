package pabs.trackstarter;

public class AudioFile {
    private String audioDirectory;
    private String audioName;
    private boolean audioDeletable;

    public AudioFile (String name,String directory, boolean deletable){
        audioName=name;
        audioDirectory=directory;
        audioDeletable=deletable;
    }

    public String getAudioName() {
        return  audioName;
    }
    public String getAudioDirectory() {
        return  audioDirectory;
    }
    public Boolean getAudioDeletable() {
        return  audioDeletable;
    }
}

