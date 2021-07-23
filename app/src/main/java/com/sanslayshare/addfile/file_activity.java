package com.sanslayshare.addfile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class file_activity extends AppCompatActivity {
    ServerSocket Server;
    String RemoteIP;
    listadapter bt;
    ListView user;
    String[] tablesarray;
    static String ReceivedFolder;
    File myDir,folder,folder2;
    String FileName;
    ProgressBar fbar;
    Uri ur ;
        int state = 0;
    int RemotePort = 6767;
    ArrayList<listcons> listconstructer  = new ArrayList<>();
    int size;
    String filename;


    FloatingActionButton snd;
    Button cancel;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_activity);
        //--------------------------------------------------


        String root = Environment.getExternalStorageDirectory().toString();
         myDir = new File(root + "/SlayShare" + "/Send"
        );
         folder = new File(root + "/SlayShare" + "/send");
         folder2 = new File(root + "/SlayShare" + "/Received");
        try{
            if(!myDir.exists())
            {
                myDir.mkdirs();
               // Toast.makeText(this, "directory created", Toast.LENGTH_SHORT).show();
            }
            if(!folder.exists());
            {
                folder.mkdirs();
              //  Toast.makeText(this, "folder1 created", Toast.LENGTH_SHORT).show();
            }
            if(!folder2.exists());
            {
                folder2.mkdirs();
              //  Toast.makeText(this, "folder2 created", Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e){
            e.printStackTrace();
        }





        //------------------------------------------------------


        String ipa = getIPAddress();
       // cancel =(Button)findViewById(R.id.cancel);
        snd = (FloatingActionButton)findViewById(R.id.sendbut);


       //  Toast.makeText(getApplicationContext(), String.valueOf(ipa), Toast.LENGTH_LONG).show();
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        sanNSD sn = new sanNSD( file_activity.this);
        String namm = BluetoothAdapter.getDefaultAdapter().getName();
        sn.registerService(namm+"@"+getIPAddress() , ipa, 6767);
       // sn.registerService("sanyu" , ipa, 6767);

        sn.DiscoverServices();


       ServerCall sec = new ServerCall(ipa, 6767);
        sec.start();
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

       // displayservices();

        FloatingActionButton fab = (FloatingActionButton)
                findViewById(R.id.fbt);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectApps();

            }
        });

        snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayservices();

            }
        });


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
      //  Bitmap gun = BitmapFactory.decodeFile("/storage/emulated/0/ak.jpg");


    }
    //////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public void displayservices() {


        final Dialog Dlg = new Dialog(this);
        LayoutInflater li = LayoutInflater.from(this);
        View wv = li.inflate(R.layout.service, null);
        Dlg.setContentView(wv);
        Dlg.getWindow().setLayout(800, 800);
       // Dlg.setFeatureDrawableAlpha(3,3);

        //Dlg.setTitle("san");

        ListView user = (ListView) wv.findViewById(R.id.serlist);


        final ArrayList<String> sname = new ArrayList<String>();
        final ArrayList<String> ipname = new ArrayList<String>();


        java.util.Iterator<Map.Entry<String, String>> iterate = sanNSD.ServMap.entrySet().iterator();
        while (iterate.hasNext()) {
            Map.Entry<String, String> mapEntry = iterate.next();
            String key = (String) mapEntry.getKey();

            String[] ServNameAndIP = key.split("@");
            String Serivename = ServNameAndIP[0];
            sname.add(Serivename);
            if (sname.isEmpty()) {
                //  Toast.makeText(socket_tictactoe.this,"empty",Toast.LENGTH_LONG).show();
            }
            String RemoteIP = ServNameAndIP[1];
            ipname.add(RemoteIP);
            if (ipname.isEmpty()) {
                // Toast.makeText(socket_tictactoe.this,"hsgdsgfuyegvdvlewgvl",Toast.LENGTH_LONG).show();
            }


        }
        user.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                RemoteIP = ipname.get(pos);


                //   Toast.makeText(socket_tictactoe.this,String.valueOf(ipname.get(pos)),Toast.LENGTH_LONG).show();

                Dlg.dismiss();
            }
        });


        ArrayAdapter<String> adpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sname);
        if (!adpt.isEmpty()) {

           user.setAdapter(adpt);
            Dlg.show();


       } else if (adpt.isEmpty()) {
          //  checkconnection();
           Toast.makeText(file_activity.this, "no devices found", Toast.LENGTH_LONG).show();


       }

        // End of DialogList() function

    }


   /* public class SendMsg extends Thread {
        String Msg;
        Socket socket;


        SendMsg(String Msg) {
            this.Msg = Msg;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(RemoteIP, RemotePort);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                //--------------Writing Bytes Stream to Socket Stream--------------------------
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                buffer = Msg.getBytes();
                byteArrayOutputStream.write(buffer);
                dos.write(buffer);
                dos.close();
                byteArrayOutputStream.close();
                //-------------------------------------------------------------------------
            } catch (IOException e) {
                e.printStackTrace();
                final String eMsg = " Send Something wrong: " + e.getMessage();
                file_activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(file_activity.this, eMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    */
    ///////////send file //////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public class ServerCall extends Thread {
        Socket pvtSocket;
        String LocalIP;
        int Port;

        ServerCall(String LocalIP, int port) {
            this.LocalIP = LocalIP;
            this.Port = port;
        }

        @Override
        public void run() {
            try {
                Server = new ServerSocket(Port, 50, InetAddress.getByName(LocalIP));
                ExecutorService pool = Executors.newFixedThreadPool(5);

                while (true) {
                    pvtSocket = Server.accept();
                    pool.execute(new ServerCallExecutor(pvtSocket));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (pvtSocket != null) {
                    try {
                        pvtSocket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////

  //-----------------------------------------------------------------------------------------------//


    public class ServerCallExecutor implements Runnable {
        private Socket pvtsocket;
        String res;
        float increment,Treceived;

        ServerCallExecutor(Socket socket) {
            this.pvtsocket = socket;
        }

        @Override
        public void run() {
            try {
                //-------------Read From Pair-----------------------------------
                DataInputStream dis = new DataInputStream(pvtsocket.getInputStream());
                //-----------Reading textnsg-------------------------------------------

                //final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                final long size=dis.readInt();
                int read = 0;
                final String Name=dis.readUTF();
                FileOutputStream fos = new FileOutputStream("/storage/emulated/0/SlayShare/Send/"+ Name );

                long remaining = size;
                increment=size/4096;
                increment=100/increment;

                while((read = dis.read(buf, 0, (int) Math.min(buf.length, remaining))) > 0) {
                    remaining -= read;

                    fos.write(buf, 0, read);
                    Treceived=Treceived+increment;
                    file_activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /*  fbar.setVisibility(View.VISIBLE);
                            fbar.setProgress((int)Treceived);
                           Toast.makeText(file_activity.this, "i am part of it", Toast.LENGTH_SHORT).show();


                           */



                        }});


                }



              /*  while((read = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                    remaining -= read;
                    fos.write(buffer, 0, read);
                }
*/
               fos.close();
                dis.close();
                file_activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(file_activity.this,Name,Toast.LENGTH_LONG).show();
                    }});

            } catch (IOException e) {
                e.printStackTrace();
                final String eMsg = " Send Something wrong: " + e.getMessage();
                file_activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(file_activity.this,eMsg,Toast.LENGTH_LONG).show();
                      //  Toast.makeText(file_activity.this, size, Toast.LENGTH_SHORT).show();
                    }});
            }
        }
    }

    ///-------------------------------------------------------------------------------------------------------////



  /////////////////////////////////////---------------------------------------------

  /*  public void displayDdata(String Name){

        Bitmap gun = BitmapFactory.decodeFile("/storage/emulated/0/aks.jpg");

        data.add(new kun(gun,Name,13,4524));
        bt= new sadp(getApplicationContext(),data);
        lv.setAdapter(bt);
        bt.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ArrayList<String> as= sanNSD.serLst;
                String arry= String.valueOf(as);
                Toast.makeText(file_activity.this,String.valueOf(as),Toast.LENGTH_LONG).show();
                displayServiceDia();
                // ArrayAdapter<String> adpt = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
                // lv.setAdapter(adpt);

            }});

    }
*/
    //------------------------------End of Server ---------------------------------------
    public String getIPAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        if (!inetAddress.getHostAddress().contains("10."))
                            ip += inetAddress.getHostAddress();
                        // ip =inetAddress.getHostAddress();
                    }
                }
            }
            return ip;
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";

        }
        return ip;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(null != data){

          /*  runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Uri ur= data.getData();
                    Toast.makeText(getApplicationContext(),String.valueOf(ur), Toast.LENGTH_LONG).show();
                    SendFileThread sm = new SendFileThread(ur);
                    sm.start();
                }
            });
*/



            ClipData clipdata = data.getClipData();
            if (clipdata != null)

                for (int i = 0; i < clipdata.getItemCount(); i++) {


                    Uri ur = clipdata.getItemAt(i).getUri();

                    long size = getFileSizeFromURI(ur);
                    int sizem = (int) (size /1000);
                    String  sizemb = String.valueOf(sizem) +" kB";
                    String filename = getFileNameFromURI(ur);
                 //   Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
                   // filetype(filename);

                    SendFileThread sm = new SendFileThread(ur);
                    sm.start();

                  //  Bitmap bmp =BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.bird);
                  //  Bitmap gun = BitmapFactory.decodeFile("/storage/emulated/0/ak.jpg");
                    try {
                        Bitmap  mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ur);
                        listcons obj  = new listcons(filename,mBitmap,  sizemb,ur);
                        listconstructer.add(obj);
                        ListView mediastore = (ListView)findViewById(R.id.serlist);

                        bt = new listadapter(getApplicationContext(),listconstructer);
                        mediastore.setAdapter(bt);

                      /*  final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                                new SwipeToDismissTouchListener<>(
                                        new ListViewAdapter(mediastore),
                                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                                            @Override
                                            public boolean canDismiss(int position) {
                                                return true;
                                            }

                                            @Override
                                            public void onDismiss(ListViewAdapter view, int position) {
                                                listconstructer.remove(position);

                                            }
                                        });

                        mediastore.setOnTouchListener(touchListener);
                        mediastore.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
                        mediastore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (touchListener.existPendingDismisses()) {
                                    touchListener.undoPendingDismiss();
                                } else {
                                    Toast.makeText(file_activity.this, "Position " + position, Toast.LENGTH_LONG).show();
                                }
                            }
                        });



                       */

                        SwipeDismissListViewTouchListener touchListener =
                                new SwipeDismissListViewTouchListener(
                                        mediastore,
                                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                            @Override
                                            public boolean canDismiss(int position) {
                                                return true;
                                            }

                                            @Override
                                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                                for (int position : reverseSortedPositions) {
                                                    listconstructer.remove(position);
                                                    bt.notifyDataSetChanged();

                                                }

                                            }
                                        });
                        mediastore.setOnTouchListener(touchListener);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // listconstructer.add(obj);
                 //   bt.notifyDataSetChanged();

                }else
            {
                Uri ur = data.getData();

              //  Toast.makeText(this, String.valueOf( ur), Toast.LENGTH_SHORT).show();

                long size = getFileSizeFromURI(ur);
                int sizem = (int) (size /1000);
                String  sizemb = String.valueOf(sizem) +" kB";
                String filename = getFileNameFromURI(ur);
               // Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
               // filetype(filename);

                SendFileThread sm = new SendFileThread(ur);
                sm.start();


               // Bitmap gun = BitmapFactory.decodeFile("/storage/emulated/0/ak.jpg");

               /* listcons obj = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    obj = new listcons(filename, gun, Math.toIntExact((long) size),ur);
                }


                */
             //   Bitmap bmp =BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.bird);

                try {
                    Bitmap  mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ur);
                    listcons obj  = new listcons(filename,mBitmap,  sizemb,ur);
                    listconstructer.add(obj);
                    ListView mediastore = (ListView)findViewById(R.id.serlist);

                    bt = new listadapter(getApplicationContext(),listconstructer);
                    mediastore.setAdapter(bt);



                    SwipeDismissListViewTouchListener touchListener =
                            new SwipeDismissListViewTouchListener(
                                    mediastore,
                                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                        @Override
                                        public boolean canDismiss(int position) {
                                            return true;
                                        }

                                        @Override
                                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                            for (int position : reverseSortedPositions) {
                                                listconstructer.remove(position);
                                                bt.notifyDataSetChanged();

                                            }

                                        }
                                    });
                    mediastore.setOnTouchListener(touchListener);





                } catch (IOException e) {
                    e.printStackTrace();
                }

           /*    listadapter  obj = new listcons(filename, gun, Math.toIntExact((long) size),ur);
                listconstructer.add(obj);
                bt.notifyDataSetChanged();

            */
            }

                }


         /*   final Dialog Dlog = new Dialog(this);
            LayoutInflater li = LayoutInflater.from(this);
            View wv = li.inflate(R.layout.medialist, null);
            Dlog.setContentView(wv);
            Dlog.getWindow().setLayout(800, 800);
            ListView mediastore = (ListView)wv.findViewById(R.id.medialist);

            bt = new listadapter(getApplicationContext(),listconstructer);

            mediastore.setAdapter(bt);
            Dlog.show();

*/

        }

    }

    public long getFileSizeFromURI(Uri uri){
        Cursor returnCursor =this.getContentResolver().query(uri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        return returnCursor.getLong(sizeIndex);
    }

    public String getFileNameFromURI(Uri uri){
        Cursor returnCursor =this.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        String mimeType =this.getContentResolver().getType(uri);
        returnCursor.moveToFirst();
        String str=returnCursor.getString(nameIndex);
        returnCursor.close();

        return str;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    public void selectApps() {

  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
  intent.addCategory(Intent.CATEGORY_OPENABLE);


   intent.setType("*/*");
   startActivityForResult(intent,1);

    }

    private class SendFileThread extends Thread {
        Uri ur;
        Socket socket;
        float incre;
        int bytesRead = -1;
        int byteread=0;
        int inh=0;
       //File fl= new File( "/storage/emulated/0/san2.jpg");
        SendFileThread(Uri Ur ) {
            this.ur=Ur;
       }
        @Override
        public void run() {
           try {
                socket = new Socket(RemoteIP, RemotePort);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                //--------------Writing Bytes Stream to Socket Stream--------------------------
//	  		    byte[] buffer = new byte[1024];
                InputStream is = getContentResolver().openInputStream(ur);
                String fnm= getFileNameFromURI(ur);
                long FileSize=getFileSizeFromURI(ur);
               // ContentResolver fis =getContentResolver().stgnew InputStream(ur);
                dos.writeInt((int) FileSize);
                dos.writeUTF(fnm);
                incre=FileSize/4096;
                incre=100/incre;
                byte[] buffer = new byte[4096];
                bytesRead = -1;
                   while ((bytesRead = is.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                    byteread=byteread+buffer.length;
                    inh=(int) (inh+incre);
                }



                dos.close();
                   //--------------------------------------------------------------------------------------------------
            } catch (IOException e) {
                e.printStackTrace();
                final String eMsg = " Send Something wrong: " + e.getMessage();
                file_activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(file_activity.this,eMsg,Toast.LENGTH_LONG).show();
                    }});
            }
        }
    }

    public void filetype(String FileName){
        String FileType=FileName.substring(FileName.lastIndexOf('.')+1);

      /*  if(FileType.equals("jpg"))
        Toast.makeText(this, "jpg", Toast.LENGTH_SHORT).show();


       */
        if(FileType.equals("bmp"))
           // bmp =BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bmp);
        if(FileType.equals("png"))
           // bmp =BitmapFactory.decodeResource(mContext.getResources(), R.drawable.png);
        if(FileType.equals("gif"))
           // bmp =BitmapFactory.decodeResource(mContext.getResources(), R.drawable.gif);

        //------------Audio-Video----------------------------------------------------
        if(FileType.equals("mp3"))
         //   Toast.makeText(this, "mp3", Toast.LENGTH_SHORT).show();





        if(FileType.equals("wav"))

        if(FileType.equals("mpg"))


        if(FileType.equals("mp4"))

        if(FileType.equals("mpeg"))

        if(FileType.equals("avi"))

        if(FileType.equals("flv"))


        if(FileType.equals("pdf"))
            Toast.makeText(this, "pdf", Toast.LENGTH_SHORT).show();

        if(FileType.equals("exe") || FileType.equals("msi"))

        if(FileType.equals("doc") || FileType.equals("docx"))

        if(FileType.equals("csv"))

        if(FileType.equals("xls") || FileType.equals("xlsx") )

        if(FileType.equals("zip"))

        if(FileType.equals("txt"))

        if(FileType.equals("ppt"))
            Toast.makeText(this, "ppt", Toast.LENGTH_SHORT).show();


    }

    }





