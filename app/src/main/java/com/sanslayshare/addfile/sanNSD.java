package com.sanslayshare.addfile;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class sanNSD {
    public static HashMap<String,String> ServMap = new  HashMap<String,String>();
    Context mContext;
    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;
    public static final String SERVICE_TYPE = "_nsdchat._tcp.";
    public static final String TAG = "NSD";
    public NsdServiceInfo mService;


    public String mServiceName;

    public sanNSD(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }
    // --------------------service registration fuction------------------------------------
    public void registerService(String ServiceName,String IP,int port) {
        UnregisterService();  // Cancel any previous registration request
        initializeRegistrationListener();
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        mServiceName=ServiceName;
        serviceInfo.setServiceName(ServiceName);
        serviceInfo.setPort(port);
        try {
            serviceInfo.setHost(InetAddress.getByName(IP));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        serviceInfo.setServiceType(SERVICE_TYPE);
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }
    //when you go out of network u gotta unregistr urserlgf
    public void UnregisterService() {
        if (mRegistrationListener != null) {
            try {
                mNsdManager.unregisterService(mRegistrationListener);
            } finally {
            }
            mRegistrationListener = null;
        }
    }

    //-------------------------------------------------------------------------------

    public void initializeNSD() {
        new TpResolveListener();
    }
    public void DiscoverServices() {
        stopDiscovery();  // Cancel any existing discovery request
        initializeDiscoveryListener();
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

    }
    //---------------------Service Discovery Initialization-------------------------------------
    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
                //Toast.makeText(mContext,"Service discovery started", Toast.LENGTH_LONG).show();
                //((MainActivity)mContext).SetStatus("Service discovery started");
            }
            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success" + service);
                setServiceInfo(service);
                if(!mServiceName.equals(service.getServiceName()))
                {  ServMap.put(service.getServiceName(),"0");}
                //  Toast.makeText(mContext,String.valueOf("Service : "+service.getServiceName()), Toast.LENGTH_LONG).show();
                //mNsdManager.resolveService(service, new TpResolveListener());
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                if (mService == service) {
                    mService = null;
                }
                //servicelost();
                //  Toast.makeText(mContext,"service lost", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
                //  Toast.makeText(mContext,serviceType+" Discovery Stopped ", Toast.LENGTH_LONG).show();
                //servicelost();
                //((MainActivity)mContext).SetStatus("Discovery stopped");
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                //  Toast.makeText(mContext,serviceType+" Discovery failed: Error code:" + errorCode, Toast.LENGTH_LONG).show();
                //((MainActivity)mContext).SetStatus("Discovery failed:"+ String.valueOf(errorCode));
                //servicelost();
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                //   Toast.makeText(mContext," Discovery failed" + errorCode, Toast.LENGTH_LONG).show();
              //  servicelost();
            }
        };
    }
    //--------------------- End of Service Discovery Initialization----------------------------
    private class TpResolveListener implements NsdManager.ResolveListener {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "Resolve failed" + errorCode);

        }

        @Override
        public void onServiceResolved(NsdServiceInfo servic) {
            //   Toast.makeText(mContext,servic.getHost().toString(), Toast.LENGTH_LONG).show();
            //   Toast.makeText(mContext,String.valueOf(servic.getPort()), Toast.LENGTH_LONG).show();
            String serviceName=servic.getServiceName();
            String ip = servic.getHost().getHostAddress();

            int prt = servic.getPort();
            String port = String.valueOf(prt);


            //  serLst.add( serviceName);

            // serLst.add(ip);

            //serLst.add(port);
        }

    }

    //-------------------------------------------------------------------------------------
    public void stopDiscovery() {
        if (mDiscoveryListener != null) {
            try {
                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            } finally {
            }
            mDiscoveryListener = null;
        }
    }
    //--------------------------------------------------------------------------------------
    public void setServiceInfo(NsdServiceInfo service) {
        this.mService=service;
    }

    public NsdServiceInfo getServiceInfos() {
        return mService;
    }
    //---------------------------------Service Registration-----------------------------------


    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                //mServiceName = NsdServiceInfo.getServiceName();
                // Log.d(TAG, "Service registered: " + mServiceName);
            }
            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Log.d(TAG, "Service registration failed: " + arg1);
            }
            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Log.d(TAG, "Service unregistered: " + arg0.getServiceName());
            }
            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "Service unregistration failed: " + errorCode);
            }
        };
    }


    //---------------------------------End of Service Registration----------------------------
    private void sendBroadcastToMainActivity(String str){
        LocalBroadcastManager lbm=LocalBroadcastManager.getInstance(mContext);
        Intent intent = new Intent("SANYU");
        intent.putExtra("CODE",str);
        intent.putExtra("NM","Renu");
        lbm.sendBroadcast(intent);
    }

}


