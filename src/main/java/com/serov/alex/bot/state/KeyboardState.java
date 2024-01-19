package com.serov.alex.bot.state;

import com.serov.alex.bot.constant.CallbackButtonConstant;

public enum KeyboardState {
    Back,
    Download,
    Forward,
    Delete,
    File;

    public static KeyboardState getState(String s)
    {
        String typeString = s.split(CallbackButtonConstant.buttonModeSeparator)[0];
        switch (typeString) {
            case CallbackButtonConstant.buttonBackData -> {
                return Back;
            }
            case CallbackButtonConstant.buttonDownloadData -> {
                return Download;
            }
            case CallbackButtonConstant.buttonForwardData -> {
                return Forward;
            }
            case CallbackButtonConstant.buttonDeleteData -> {
                return Delete;
            }
        }
        return File;
    }
}
