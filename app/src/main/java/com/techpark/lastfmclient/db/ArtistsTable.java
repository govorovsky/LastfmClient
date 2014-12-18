package com.techpark.lastfmclient.db;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.techpark.lastfmclient.api.artist.Artist;

/**
 * Created by max on 14/11/14.
 */
public class ArtistsTable implements BaseColumns {

    private ArtistsTable() {}

    public static final String TABLE_NAME = "artist";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/artist");
    public static final Uri CONTENT_URI_ID_ARTIST = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/artist/");

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_IMAGE_SMALL = "image_small";
    public static final String COLUMN_IMAGE_MEDIUM = "image_medium";
    public static final String COLUMN_IMAGE_LARGE = "image_large";
    public static final String COLUMN_IMAGE_EXTRALARGE = "image_extralarge";
    public static final String COLUMN_IMAGE_MEGA = "image_mega";
    public static final String COLUMN_BIO_SUMMARY = "bio_summary";
    public static final String COLUMN_BIO_CONTENT = "bio_content";
    public static final String COLUMN_TAGS = "tags";
    public static final String COLUMN_SIMILARS = "similars";

    public static final String SQL_CREATE_ARTIST_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT UNIQUE, "
            + COLUMN_URL + " TEXT, "
            + COLUMN_IMAGE_SMALL + " TEXT, "
            + COLUMN_IMAGE_MEDIUM + " TEXT, "
            + COLUMN_IMAGE_LARGE + " TEXT, "
            + COLUMN_IMAGE_EXTRALARGE + " TEXT, "
            + COLUMN_IMAGE_MEGA + " TEXT, "
            + COLUMN_BIO_SUMMARY + " TEXT, "
            + COLUMN_BIO_CONTENT + " TEXT, "
            + COLUMN_TAGS + " TEXT, "
            + COLUMN_SIMILARS + " TEXT "
            + ");";

    public static ContentValues getContentValues(Artist artist) {
        ContentValues content = new ContentValues(Artist.ARTIST_SIZE);
        content.put(ArtistsTable.COLUMN_NAME, artist.getArtistName());
        content.put(ArtistsTable.COLUMN_URL, artist.getUrl());
        content.put(ArtistsTable.COLUMN_IMAGE_SMALL, artist.getImage(Artist.ImageSize.SMALL));
        content.put(ArtistsTable.COLUMN_IMAGE_MEDIUM, artist.getImage(Artist.ImageSize.MEDIUM));
        content.put(ArtistsTable.COLUMN_IMAGE_LARGE, artist.getImage(Artist.ImageSize.LARGE));
        content.put(ArtistsTable.COLUMN_IMAGE_EXTRALARGE, artist.getImage(Artist.ImageSize.EXTRALARGE));
        content.put(ArtistsTable.COLUMN_IMAGE_MEGA, artist.getImage(Artist.ImageSize.MEGA));
        content.put(ArtistsTable.COLUMN_BIO_SUMMARY, artist.getBioSummary());
        content.put(ArtistsTable.COLUMN_BIO_CONTENT, artist.getBioContent());

        StringBuilder tagsBuilder = new StringBuilder();
        for (String s : artist.getTags())
            tagsBuilder.append(s).append(",");
        content.put(ArtistsTable.COLUMN_TAGS, tagsBuilder.toString());

        StringBuilder similarsBuilder = new StringBuilder();
        for (String s : artist.getSimilars())
            tagsBuilder.append(s).append(",");
        content.put(ArtistsTable.COLUMN_SIMILARS, similarsBuilder.toString());

        return content;
    }


}
