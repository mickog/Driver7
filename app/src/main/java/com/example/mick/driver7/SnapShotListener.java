package com.example.mick.driver7;

import java.util.ArrayList;

/**
 * Created by Mick on 26/03/2017.
 */

public interface SnapShotListener{
    void onListFilled(ArrayList<String> arrayLat,ArrayList<String> arrayLon);
    void onFailure();
}