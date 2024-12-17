package com.nonplaystoreapps;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NonPlayStoreApps extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // get ListView
        ListView listView = findViewById(R.id.app_list);

        // get PackageManager
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

        // list to store app names
        List<String> nonPlayStoreApps = new ArrayList<>();

        for (PackageInfo packageInfo : installedPackages) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            
            try {
                // check if the app is not a system app
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    // additional checks to identify non play store apps
                    String installer = packageManager.getInstallerPackageName(appInfo.packageName);
                    
                    // explicitly check against known play store package names
                    if (installer == null || 
                        !installer.equals("com.android.vending") && 
                        !installer.equals("com.google.android.packageinstaller")) {
                        
                        String appName = appInfo.loadLabel(packageManager).toString();
                        nonPlayStoreApps.add(appName + " (" + appInfo.packageName + ")");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // display apps in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nonPlayStoreApps);
        listView.setAdapter(adapter);
    }
}
