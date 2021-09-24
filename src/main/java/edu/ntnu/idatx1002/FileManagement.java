package edu.ntnu.idatx1002;

import java.io.*;
/**
 * This class provides methods to store the tasks in a folder on the users pc
 *  @author KOHORT 1.7
 *  @version 2020-04-26
 */
public class FileManagement {
    private String folderPath, filePath;
    private final String settingsPath;
    List mainList;

    public FileManagement(){
        settingsPath = System.getProperty("user.home") + "\\Documents\\2Do\\Settings\\filepath";
        startupSettingsChecker();

        if (readFilepath().isEmpty() || readFilepath().equalsIgnoreCase("")){
            this.folderPath = System.getProperty("user.home") + "\\Documents\\2Do\\Tasks";
            this.filePath = folderPath + "\\output";

        }else{
            this.filePath = readFilepath();
            this.folderPath = filePath.replace("\\output", "");
        }
        setFilePath(filePath);
        File folder = new File(folderPath);
        File file = new File(filePath);

        mainList = startupFileChecker(folder, file, filePath);
    }

    public List getMainList(){
        return mainList;
    }
    public String getFilePath(){
        return filePath;
    }
    public List getSaveList() {
        return mainList;
    }

    /**
     * Method checks if folder where data should be stored exists,
     * if not it creates necessary folders and empty file, and returns empty list.
     * If folder and file already exists it reads the file and returns read data.
     * @param folder File folder where data is stored
     * @param file File file in folder
     * @param filePath String path to file from C: drive
     * @return List List of tasks saved
     */
    public List startupFileChecker(File folder, File file, String filePath){
        List newList = new List("Main");

        if(!(folder.exists())){
            try {
                folder.mkdirs();

                save(newList);
                return newList;
            }catch (Exception e){
                e.printStackTrace();
                return newList;
            }
        }
        else if(!(file.exists())){
            try {
                save(newList);
                return newList;
            }catch (Exception e){
                e.printStackTrace();
                return newList;
            }
        }
        else {
            newList = read(filePath);
            return newList;
        }
    }
    /**
     * Method overwrites old file with new data
     * @param mainList List to be saved
     */
    public void save(List mainList){
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mainList);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }


    }
    /**
     * Method reads saved file and returns List
     * @param path String path to be read from
     * @return List with stored data
     */
    public List read(String path){
        List secondaryList = new List("Main");
        try {
            ObjectInputStream roos = new ObjectInputStream(new FileInputStream(path));

            int read = roos.read();
            if(read == -1){}

            secondaryList = (List) roos.readObject();
            roos.close();

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return secondaryList;
    }
    /**
     * Method gets filepath settings from locally stored settings and applies them to variables.
     */
    public void startUpFile(){
        this.folderPath = readFilepath().replace("\\output", "");
        this.filePath = this.folderPath + "\\output";
        File folder = new File(this.folderPath);
        File file = new File(this.filePath);

        this.mainList = startupFileChecker(folder, file, this.filePath);
    }

    /**
     * Method checks if folder where settings should be stored exists,
     * if not it creates necessary folders and default filepath.
     * If folder and file already exists it reads the file and returns read data.
     * @return String path to stored data
     */
    public String startupSettingsChecker(){


        if(!(new File(settingsPath.replace("\\filepath", "")).exists())){
            try {
                new File(settingsPath.replace("\\filepath", "")).mkdirs();

                saveFilePath("");
                return "";
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }
        else if(!(new File(settingsPath).exists())){
            try {
                saveFilePath("");
                return "";
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }else {
            return  readFilepath();
        }
    }
    /**
     * Method reads filepath from settings and returns read filepath
     * @return String stored file path
     */
    public String readFilepath(){
        String readS = "";
        try {

            ObjectInputStream roos = new ObjectInputStream(new FileInputStream(settingsPath));
            int read = roos.read();
            if(read == -1){}

            readS = (String) roos.readObject();
            roos.close();


        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return readS;
    }
    /**
     * Method gets new filepath and changes local variable
     * @param newPath String new path
     */
    public void setFilePath(String newPath){
        this.filePath = newPath;
    }
    /**
     * Method commits changed filepath for data storage to settings and overwrites old file
     * @param newPath saves new path to file on drive
     */
    public void saveFilePath(String newPath){
        try {
            String SETTINGSPATH = System.getProperty("user.home") + "\\Documents\\2Do\\Settings\\filepath";
            FileOutputStream fos = new FileOutputStream(SETTINGSPATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(newPath);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public void resetFilePath(){
        saveFilePath("");
    }

}

