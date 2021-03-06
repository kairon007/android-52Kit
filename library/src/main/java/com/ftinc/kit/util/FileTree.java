/*
 * Copyright (c) 2015 52inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ftinc.kit.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.ftinc.kit.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Timber logger that appends log statments to a file
 *
 */
public class FileTree extends Timber.Tree {

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("\\$\\d+$");
    private static final ThreadLocal<String> NEXT_TAG = new ThreadLocal<>();

    private String mDirName;
    private String mFileName;

    /**
     * Constructor
     *
     * @param filename      the name of the logfile
     */
    public FileTree(Context ctx, String filename){
        mFileName = filename;
        mDirName = ctx.getString(R.string.app_name);
    }

    /***********************************************************************************************
     *
     * Tree Methods
     *
     */

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        throwShade(priority, tag, message, t);
    }

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */

    private void throwShade(int priority, String tag, String message, Throwable t) {
        if (message == null || message.length() == 0) {
            if (t != null) {
                message = Log.getStackTraceString(t);
            } else {
                // Swallow message if it's null and there's no throwable.
                return;
            }
        } else if (t != null) {
            message += "\n" + Log.getStackTraceString(t);
        }

        if (message.length() < 4000) {
            printLog(priority, tag, message);
        } else {
            // It's rare that the message will be this large, so we're ok with the perf hit of splitting
            // and calling Log.println N times.  It's possible but unlikely that a single line will be
            // longer than 4000 characters: we're explicitly ignoring this case here.
            String[] lines = message.split("\n");
            for (String line : lines) {
                printLog(priority, tag, line);
            }
        }
    }

    /**
     * Convienence function for logging to the crashlytics server
     *
     * @param priority      the logging priority
     * @param tag           the log tag
     * @param message       the log message
     */
    private void printLog(int priority, String tag, String message){
        if(FileUtils.checkMediaState() == FileUtils.READ_WRITE) {
            File dir = new File(Environment.getExternalStorageDirectory(), mDirName);
            if(!dir.exists()){
                dir.mkdir();
            }

            File logFile = new File(dir, mFileName);
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {

                String msg = String.format("[%s][%s][%s]", getNameForPriority(priority), tag, message);
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(msg);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getNameForPriority(int priority){
        switch (priority){
            case Log.VERBOSE:
                return "VERBOSE";
            case Log.DEBUG:
                return "DEBUG";
            case Log.INFO:
                return "INFO";
            case Log.ASSERT:
                return "ASSERT";
            case Log.WARN:
                return "WARN";
            case Log.ERROR:
                return "ERROR";
            default:
                return "UNKNOWN";
        }
    }
}