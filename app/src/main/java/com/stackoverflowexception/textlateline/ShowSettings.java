package com.stackoverflowexception.textlateline;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ShowSettings extends PreferenceActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
}
