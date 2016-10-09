package com.example.sneha.trails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucascassiano on 10/8/16.
 */

public class SavedPaths {

    private static SavedPaths instance;
    private List<List<LocationData>> paths;
    private int selectedPath;

    private SavedPaths() {
        paths = new ArrayList<>();
    }
    public static SavedPaths getInstance() {

        if(instance == null) {
            instance = new SavedPaths();
        }

        return instance;
    }

    public List<List<LocationData>> getPaths() {
        return paths;
    }

    public void setSelectedPath(int selectedPath){
        this.selectedPath = selectedPath;
    }

    public int getSelectedPath(){
        return selectedPath;
    }

}
