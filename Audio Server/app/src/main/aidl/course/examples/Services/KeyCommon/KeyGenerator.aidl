package course.examples.Services.KeyCommon;

interface KeyGenerator {
    void getPlay(String str);
    void getPause();
    void getResume();
    void getStop();
    String[] getAllData();
}