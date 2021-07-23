package com.sanslayshare.addfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static java.lang.String.valueOf;

public class listadapter extends BaseAdapter {

    Context cont;
    LayoutInflater layoutInflater;
    List<listcons> listcon;

    public listadapter(Context cont , List<listcons> listcon)
    {
        this.cont= cont;
        this.listcon = listcon;
        layoutInflater= LayoutInflater.from(cont);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return  listcon.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listcon.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class ViewHolder{
        TextView filename,size;
        ImageView photo;
        TextView ur;



    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ViewGroup vg = null;
        if (convertView == null) {
            holder = new ViewHolder();



            convertView = layoutInflater.inflate(R.layout.listitems, vg);
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            holder.filename = (TextView) convertView.findViewById(R.id.filename);
            holder.size = (TextView) convertView.findViewById(R.id.filesize);

               holder.ur =(TextView)convertView.findViewById(R.id.ur);
            convertView.setTag(holder);
           // convertView.setBackgroundResource(R.drawable.listview_selector);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.photo.setImageBitmap(listcon.get(position).getphoto());
        holder.filename.setText(listcon.get(position).filename);
        holder.ur.setText(valueOf( listcon.get(position).ur));



        final listcons listarray = listcon.get(position);

        // holder.bt.setOnClickListener((View.OnClickListener) listcon.get(position).bt);


    /*  convertView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             // Toast.makeText(cont,listarray.getfilename() , Toast.LENGTH_SHORT).show();
              String FileName = listarray.getfilename();
              Uri geturi = listarray.geturi();


              String FileType=FileName.substring(FileName.lastIndexOf('.')+1);
              if(FileType.equals("jpg")) {

                  //---------------------------------------------------------------



                  //-----------------------------------------
                  Toast.makeText(cont, FileType, Toast.LENGTH_SHORT).show();
                  /////////////////////////////////
                  Intent intent = new Intent();
                  intent .setAction(Intent.ACTION_VIEW);
                 // intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //  intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  Intent intent1 = intent.setDataAndType(Uri.parse(String.valueOf(geturi)),"image/*");
                  cont.startActivity(intent);
                  cont.startActivity(intent1);








              }
             /* if(FileType.equals("mp4")){
                  File file  =new File(String.valueOf(geturi));
                  Intent intent = new Intent(Intent.ACTION_VIEW);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  Intent intent1 = intent.setDataAndType(FileProvider.getUriForFile(cont, cont.getApplicationContext().getPackageName() + ".provider", file));
                  cont.startActivity(intent);
                  con

              }



          }
      });

     */



        holder.size.setText(valueOf(listcon.get(position).size));
        return convertView;
        }
}
