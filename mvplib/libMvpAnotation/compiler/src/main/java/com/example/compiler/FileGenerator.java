package com.example.compiler;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public interface FileGenerator {

     Set<String> getSupportedAnnotationTypes();

     boolean process(Elements elements, Messager messager, Filer filer, Set<? extends TypeElement> set, RoundEnvironment roundEnvironment);

}
