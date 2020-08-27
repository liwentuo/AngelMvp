package com.example.compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class ProcessorUtils  {

    private Elements mElementUtils;
    private Messager mMessager;
    private Filer mFiler;


    private ArrayList<FileGenerator> mGenerators;

    public ProcessorUtils(Elements mElementUtils, Messager mMessager, Filer mFiler) {
        this.mElementUtils = mElementUtils;
        this.mMessager = mMessager;
        this.mFiler = mFiler;



        mGenerators = new ArrayList<>();


        mGenerators.add(new ConfigGenerator());
        mGenerators.add(new EntityGenerator());


    }


    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        for(FileGenerator generator : mGenerators){
            types.addAll(generator.getSupportedAnnotationTypes());
        }
        return types;
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for(FileGenerator generator : mGenerators){
            generator.process(mElementUtils,mMessager,mFiler,set,roundEnvironment);
        }

        return false;
    }
}
