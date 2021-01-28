package com.github.vitorm3lo.jbossplugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Instance {

    private String serverName;
    private int debugPort;
    private File serverPath;
    private List<File> deployablePath;

}
