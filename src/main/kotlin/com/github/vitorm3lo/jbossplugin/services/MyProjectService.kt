package com.github.vitorm3lo.jbossplugin.services

import com.intellij.openapi.project.Project
import com.github.vitorm3lo.jbossplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
