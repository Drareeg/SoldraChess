/*
 * The MIT License
 *
 * Copyright 2014 Dries Weyme & Geerard Ponnet.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Shared.Chess;
import javafx.util.Pair;

/**
 *
 * @author Geerard
 */
public class Coordinate extends Pair<Integer, Integer> {

    public int getRow() {
        return getKey();
    }

    public int getCol() {
        return getValue();
    }

    public Coordinate(Integer row, Integer col) {
        super(row, col);
    }

    Coordinate add(Coordinate fromCoord) {
        return new Coordinate(getKey() + fromCoord.getKey(), getValue() + fromCoord.getValue());
    }

    private final static String[] cols = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};

    @Override
    public String toString() {
        return cols[getCol()].toUpperCase() + (9 - ((getRow() + 1)));
    }

}
