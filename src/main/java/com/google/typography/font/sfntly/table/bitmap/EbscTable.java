/*
 * Copyright (C) 2016. JRummy Apps, Inc. - All Rights Reserved
 *
 * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 * is strictly prohibited.
 *
 * File created on 4/7/16 5:17 PM by Jared Rummler.
 */

package com.google.typography.font.sfntly.table.bitmap;

import com.google.typography.font.sfntly.data.FontData;
import com.google.typography.font.sfntly.data.ReadableFontData;
import com.google.typography.font.sfntly.data.WritableFontData;
import com.google.typography.font.sfntly.table.Header;
import com.google.typography.font.sfntly.table.SubTable;
import com.google.typography.font.sfntly.table.Table;

/**
 * @author Stuart Gill
 */
public class EbscTable extends Table {

  enum Offset {
    // header
    version(0),
    numSizes(FontData.DataSize.Fixed.size()),
    headerLength(numSizes.offset + FontData.DataSize.ULONG.size()),
    bitmapScaleTableStart(headerLength.offset),

    // bitmapScaleTable
    bitmapScaleTable_hori(0),
    bitmapScaleTable_vert(EblcTable.Offset.sbitLineMetricsLength.offset),
    bitmapScaleTable_ppemX(bitmapScaleTable_vert.offset
        + EblcTable.Offset.sbitLineMetricsLength.offset),
    bitmapScaleTable_ppemY(bitmapScaleTable_ppemX.offset + FontData.DataSize.BYTE.size()),
    bitmapScaleTable_substitutePpemX(bitmapScaleTable_ppemY.offset + FontData.DataSize.BYTE.size()),
    bitmapScaleTable_substitutePpemY(bitmapScaleTable_substitutePpemX.offset
        + FontData.DataSize.BYTE.size()),
    bitmapScaleTableLength(bitmapScaleTable_substitutePpemY.offset + FontData.DataSize.BYTE.size());

    final int offset;

    private Offset(int offset) {
      this.offset = offset;
    }
  }

  /**
   * @param header
   * @param data
   */
  private EbscTable(Header header, ReadableFontData data) {
    super(header, data);
  }

  public int version() {
    return this.data.readFixed(Offset.version.offset);
  }

  public int numSizes() {
    return this.data.readULongAsInt(Offset.numSizes.offset);
  }

  public BitmapScaleTable bitmapScaleTable(int index) {
    if (index < 0 || index > this.numSizes() - 1) {
      throw new IndexOutOfBoundsException(
          "BitmapScaleTable index is outside the bounds of available tables.");
    }
    return new BitmapScaleTable(this.data,
        Offset.bitmapScaleTableStart.offset + index * Offset.bitmapScaleTableLength.offset);
  }

  public static class BitmapScaleTable extends SubTable {

    protected BitmapScaleTable(ReadableFontData data, int offset) {
      super(data, offset, Offset.bitmapScaleTableLength.offset);
    }

    public int ppemX() {
      return this.data.readByte(Offset.bitmapScaleTable_ppemX.offset);
    }

    public int ppemY() {
      return this.data.readByte(Offset.bitmapScaleTable_ppemY.offset);
    }

    public int substitutePpemX() {
      return this.data.readByte(Offset.bitmapScaleTable_substitutePpemX.offset);
    }

    public int substitutePpemY() {
      return this.data.readByte(Offset.bitmapScaleTable_substitutePpemY.offset);
    }
  }

  // TODO(stuartg): currently the builder just builds from initial data
  // - need to make fully working but few if any examples to test with
  public static class Builder extends Table.Builder<EbscTable> {

    /**
     * Create a new builder using the header information and data provided.
     *
     * @param header
     *     the header information
     * @param data
     *     the data holding the table
     * @return a new builder
     */
    public static Builder createBuilder(Header header, WritableFontData data) {
      return new Builder(header, data);
    }

    /**
     * @param header
     * @param data
     */
    protected Builder(Header header, WritableFontData data) {
      super(header, data);
    }

    /**
     * @param header
     * @param data
     */
    protected Builder(Header header, ReadableFontData data) {
      super(header, data);
    }

    @Override
    protected EbscTable subBuildTable(ReadableFontData data) {
      return new EbscTable(this.header(), data);
    }

    @Override
    protected void subDataSet() {
      // NOP
    }

    @Override
    protected int subDataSizeToSerialize() {
      return 0;
    }

    @Override
    protected boolean subReadyToSerialize() {
      return false;
    }

    @Override
    protected int subSerialize(WritableFontData newData) {
      return 0;
    }

  }
}