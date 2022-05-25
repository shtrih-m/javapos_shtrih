
import android.content.ContentProvider;
import android.database.Cursor;
import android.net.Uri;
import android.os.ICancellationSignal;


public final class LibraryContentProvider extends ContentProvider {

    public LibraryContentProvider() {
    }

    @Override
    public boolean onCreate() {
        // get the context (Application context)
        Context context = getContext();
        // initialize whatever you need
    }

    @Nullable
    @Override
    public Cursor query(String callingPkg, Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder,
                        ICancellationSignal cancellationSignal) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}