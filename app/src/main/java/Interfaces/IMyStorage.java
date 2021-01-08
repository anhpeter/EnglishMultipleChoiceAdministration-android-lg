package Interfaces;

public interface IMyStorage {
    public void uploadedCallback(String url, int code);
    public void progressCallback(double progress, int code);
    public void deletedCallback(int code);
}
