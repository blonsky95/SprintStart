package pabs.trackstarter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AudioFileAdapter extends ArrayAdapter<AudioFile> {

    ArrayList<AudioFile> audioListL;
    Context contextL;
    int audioID;

    public AudioFileAdapter(Activity context, ArrayList<AudioFile> audioList, int soundIdentifier) {
        super(context, 0, audioList);
        audioListL = audioList;
        contextL = context;
        audioID = soundIdentifier;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listViewItem = convertView;
        final int pos = position;

        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        AudioFile currentAudioFile = getItem(position);


        final String fileName = currentAudioFile.getAudioName();
        final String fileDir = currentAudioFile.getAudioDirectory();
        final boolean deletable = currentAudioFile.getAudioDeletable();

        TextView audioName = listViewItem.findViewById(R.id.sound_name);
        audioName.setText(fileName);

        audioName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSound(fileDir, fileName, audioID,contextL);
            }
        });

        ImageView deleteBtn = listViewItem.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteFile(fileDir, deletable, pos);

            }
        });
        return listViewItem;
    }

    private void deleteFile(final String fileDir, boolean deletable, int position) {
        final int pos = position;
        if (deletable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(contextL);

            builder.setMessage(contextL.getResources().getString(R.string.delete_confirm));
            builder.setPositiveButton(contextL.getResources().getString(R.string.delete_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    audioListL.remove(pos);
                    notifyDataSetChanged();
                    File fileDelete = new File(fileDir);
                    if (fileDelete.exists()) {
                        boolean deleted = fileDelete.delete();
                    }
                    Toast.makeText(contextL, contextL.getResources().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();

                }
            });

            builder.setNegativeButton(contextL.getResources().getString(R.string.delete_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            builder.show();
            return;
        }
        Toast.makeText(contextL, contextL.getResources().getString(R.string.non_deletable), Toast.LENGTH_SHORT).show();

    }

    private void saveSound(final String fileDir, final String fileName, final int SoundID, final Context con) {

        AlertDialog.Builder builder = new AlertDialog.Builder(contextL);

        builder.setMessage(contextL.getResources().getString(R.string.sound_selected1));
        builder.setPositiveButton(contextL.getResources().getString(R.string.delete_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (SoundID != 0) {
                    if (SoundID == 1) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(contextL);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("sound1", fileDir);
                        editor.apply();
                        Intent intent1 = new Intent(getContext(), SoundEditor.class);
                        con.startActivity(intent1);
                        ((SoundSelector)contextL).finish();
                    }
                    if (SoundID == 2) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(contextL);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("sound2", fileDir);
                        editor.apply();
                        Intent intent2 = new Intent(getContext(), SoundEditor.class);
                        con.startActivity(intent2);
                        ((SoundSelector)contextL).finish();

                    }
                    if (SoundID == 3) {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(contextL);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("sound3", fileDir);
                        editor.apply();
                        Intent intent3 = new Intent(getContext(), SoundEditor.class);
                        con.startActivity(intent3);
                        ((SoundSelector)contextL).finish();

                    }
                    Toast.makeText(contextL, contextL.getResources().getString(R.string.sound_selected_success), Toast.LENGTH_SHORT).show();

                }

            }
        });

        builder.setNegativeButton(contextL.getResources().getString(R.string.delete_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();


    }

}
