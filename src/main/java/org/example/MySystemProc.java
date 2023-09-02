package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface MySystemProc {
    void executeProcedure();

    void fileWrite(boolean append, String securePath, String name, String password);

    void fileRead() throws FileNotFoundException;

    void runAllProc() throws IOException;
}
