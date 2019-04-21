package semanticalwaysauthentic.co.listbarang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ListBarangAdapter extends RecyclerView.Adapter<ListBarangAdapter.WordViewHolder>{

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView idItemView;
        public final TextView barangItemView;
        public final TextView wattItemView;
        public final TextView durasiItemView;
        Button delete_button;
        Button edit_button;

        public WordViewHolder(View itemView) {
            super(itemView);
            idItemView = (TextView) itemView.findViewById(R.id.id);
            barangItemView = (TextView) itemView.findViewById(R.id.word);
            wattItemView = (TextView) itemView.findViewById(R.id.v_watt);
            durasiItemView = (TextView) itemView.findViewById(R.id.v_durasi);
            delete_button = (Button)itemView.findViewById(R.id.delete_button);
            edit_button = (Button)itemView.findViewById(R.id.edit_button);
        }
    }

    private static final String TAG = ListBarangAdapter.class.getSimpleName();

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_WATT = "WATT";
    public static final String EXTRA_DURASI = "DURASI";

    private final LayoutInflater mInflater;
    Context mContext;

    ListBarangOpenHelper mDB;

    public ListBarangAdapter(Context context, ListBarangOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.itembarang, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        itembarang current = mDB.query(position);
        holder.idItemView.setText(current.getSid()+". ");
        holder.barangItemView.setText(current.getWord());
        holder.wattItemView.setText(current.getSWatt());
        holder.durasiItemView.setText(current.getSDurasi());
        // Keep a reference to the view holder for the click listener
        final WordViewHolder h = holder; // needs to be final for use in callback

        // Attach a click listener to the DELETE button.
        holder.delete_button.setOnClickListener(new MyButtonOnClickListener(
                current.getId(), null, current.getWatt(), current.getDurasi())  {
            @Override
            public void onClick(View v ) {
                // You have to get the position like this, you can't hold a reference
                Log.d (TAG + "onClick", "VHPos " + h.getAdapterPosition() + " ID " + id);
                int deleted = mDB.delete(id);
                int updated = mDB.update(id,word, watt, durasi);
                if (deleted >= 0) {
                    notifyItemRemoved(h.getAdapterPosition());
                }
            }
        });

        // Attach a click listener to the EDIT button.
        holder.edit_button.setOnClickListener(new MyButtonOnClickListener(
                current.getId(), current.getWord(), current.getWatt(), current.getDurasi()) {
            @Override
            public void onClick(View v) {
                String EXTRA_POSITION = "POSITION";
                Intent intent = new Intent(mContext, EditListBarang.class);
                intent.putExtra(EXTRA_ID, id);
                intent.putExtra(EXTRA_POSITION, h.getAdapterPosition());
                intent.putExtra(EXTRA_WORD, word);
                intent.putExtra(EXTRA_WATT, Swatt);
                intent.putExtra(EXTRA_DURASI, Sdursai);
                // Start an empty edit activity.
                ((Activity) mContext).startActivityForResult(
                        intent, MainActivity.WORD_EDIT);
            }
        });

    }



    @Override
    public int getItemCount() {
        // Placeholder so we can see some mock data.
        return (int) mDB.count();
    }


}
