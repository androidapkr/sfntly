/*
 * Copyright (C) 2016. JRummy Apps, Inc. - All Rights Reserved
 *
 * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 * is strictly prohibited.
 *
 * File created on 4/7/16 5:17 PM by Jared Rummler.
 */

package com.google.typography.font.sfntly.table.opentype.component;

import com.google.typography.font.sfntly.data.WritableFontData;

interface Record {

  int writeTo(WritableFontData newData, int base);
}