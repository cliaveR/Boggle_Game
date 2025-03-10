package universalModels;

import java.util.List;

public class PlayerInRound {
    public int pid;
    List<String> wordListOfRound;

    public PlayerInRound() {
    }

    public PlayerInRound(int pid, List<String> wordListOfRound) {
        this.pid = pid;
        this.wordListOfRound = wordListOfRound;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<String> getWordListOfRound() {
        return wordListOfRound;
    }

    public void setWordListOfRound(List<String> wordListOfRound) {
        this.wordListOfRound = wordListOfRound;
    }
}
