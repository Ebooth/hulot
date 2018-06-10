package fr.ec.producthunt.data.database;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.ec.producthunt.data.model.Collection;






public class CollectionDao  {
    private final ProductHuntDbHelper productHuntDbHelper;


    public CollectionDao(ProductHuntDbHelper productHuntDbHelper) {
        this.productHuntDbHelper = productHuntDbHelper;
    }

    public long save(Collection collection) {
        return productHuntDbHelper.getWritableDatabase()
                .replace(DataBaseContract.CollectionTable.TABLE_NAME, null, collection.toContentValues());
    }

    public List<Collection> retrieveCollections() {


        Cursor cursor = productHuntDbHelper.getReadableDatabase()
                .query(DataBaseContract.CollectionTable.TABLE_NAME,
                        DataBaseContract.CollectionTable.PROJECTIONS,
                        null, null, null, null, null);

        List<Collection> collections = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {
            do {

                Collection collection = new Collection();

                collection.setId(cursor.getInt(0));
                collection.setName(cursor.getString(1));
                collection.setTitle(cursor.getString(2));
                collection.setImageUrl(cursor.getString(3));

                collections.add(collection);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return collections;
    }
}
