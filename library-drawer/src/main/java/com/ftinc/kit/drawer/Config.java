/*
 * Copyright © 52inc 2015.
 * All rights reserved.
 */

package com.ftinc.kit.drawer;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftinc.kit.drawer.items.DrawerItem;
import com.ftinc.kit.util.UIUtils;

import java.util.List;

/**
 * The abstract configuration class that instructs the drawer on how to lay itself out
 *
 * Project: TradeVersity
 * Package: com.ftinc.tradeversity.ui.widget.drawer
 * Created by drew.heavner on 5/11/15.
 */
public abstract class Config {

    // delay to launch nav drawer item, to allow close animation to play
    private static final long NAVDRAWER_LAUNCH_DELAY = 250;
    private static final long MAIN_CONTENT_FADEOUT_DURATION = 150;
    private static final long MAIN_CONTENT_FADEIN_DURATION = 250;

    /**
     * Inflate the drawer items to construct the drawer with
     *
     * @param items     the items to inflate the drawer with
     */
    protected abstract void inflateItems(List<DrawerItem> items);

    /**
     * Called when a user selects a drawer item from the drawer
     *
     * @param item      the item that was clicked
     */
    protected abstract void onDrawerItemClicked(DrawerItem item);

    /**
     * Inflate the header for drawer, return null for no header
     *
     * @param inflater      the layout inflater
     * @param parent        the parent layout
     * @return              the header view, or null for none
     */
    protected abstract View inflateHeader(LayoutInflater inflater, ViewGroup parent);

    /**
     * Override this to bind data to a previously inflated header view
     *
     * @param headerView        the inflated header view to bind data to
     */
    protected void bindHeader(View headerView){}

    /**
     * Inflate the footer for the drawer, return null for no footer
     *
     * @param inflater      the layout inflater
     * @param parent        the parent layout that the footer will be placed in
     * @return              the footer view, or null for none
     */
    protected abstract View inflateFooter(LayoutInflater inflater, ViewGroup parent);

    /***********************************************************************************************
     *
     * Optional Configurations
     *
     */

    /**
     * Override this to bind data to the footer view
     *
     * @param footerView        the footer view to bind data to
     */
    protected void bindFooter(View footerView){}

    /**
     * Override to get notified when the the nav drawer's system bar insets callback
     * is called so we can manipulate the drawer header into drawing behind the status
     * bar
     *
     * @param insets        the status bar insets
     */
    protected void onInsetsChanged(View headerView, Rect insets){}

    /**
     * Get the delay in milliseconds before the intent to open a new screen is launched
     * so that the drawer can animate close before launch
     *
     * Override to implement your own delay
     *
     * @return      the delay in milliseconds
     */
    protected long getLaunchDelay(){
        return NAVDRAWER_LAUNCH_DELAY;
    }

    /**
     * Get the main content fade out duration
     *
     * Override to implement your own
     *
     * @return      the fade out duration, or -1 for no duration
     */
    protected long getFadeOutDuration(){
        return MAIN_CONTENT_FADEOUT_DURATION;
    }

    /**
     * Get the main content fade in duration
     *
     * Override to implement your own
     *
     * @return      the fade in duration, or -1 for no duration
     */
    protected long getFadeInDuration(){
        return MAIN_CONTENT_FADEIN_DURATION;
    }

    /**
     * Set the status bar color background that the DrawerLayout should render, or return -1
     * to not render at all
     *
     * @param ctx       the context reference to load color resources with
     * @return          the color, or -1 for none
     */
    protected int getStatusBarColor(Context ctx){
        return UIUtils.getColorAttr(ctx, R.attr.colorPrimaryDark);
    }

    /**
     * Return whether or not we should animate the Hamburger Icon when the drawer
     * is being dragged
     *
     * @return      true for animating, false otherwise
     */
    protected boolean shouldAnimateIndicator(){
        return true;
    }

}
