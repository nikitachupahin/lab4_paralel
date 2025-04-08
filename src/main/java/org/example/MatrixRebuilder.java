package org.example;
import java.util.concurrent.atomic.AtomicLong;

public class MatrixRebuilder {
    private final int[][] matrixArray;
    private final int matrixSize;
    private final AtomicLong rebuiltCount = new AtomicLong(0);

    public MatrixRebuilder(int matrixSize, int[][] matrixArray) {
        this.matrixSize = matrixSize;
        this.matrixArray = matrixArray;
    }

    public int[][] getMatrixArray() {
        return matrixArray;
    }

    public int getMatrixSize() {
        return matrixSize;
    }

    public long getRebuildCount() {
        return rebuiltCount.get();
    }

    public void parallelRebuildMatrix(int threadId, int amountOfThreads) {
        for (int i = threadId; i < matrixSize; i += amountOfThreads) {
            for (int j = i + 1; j < matrixSize; j++) {
                if (i != j) {
                    int tempValue = matrixArray[i][j];
                    matrixArray[i][j] = matrixArray[j][i];
                    matrixArray[j][i] = tempValue;
                    rebuiltCount.incrementAndGet();
                }
            }
        }
    }
}
