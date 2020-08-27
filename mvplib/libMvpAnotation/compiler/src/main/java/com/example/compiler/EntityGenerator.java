package com.example.compiler;

import com.m.k.anotaion.BaseUrl;
import com.m.k.anotaion.MvpEntity;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class EntityGenerator implements FileGenerator {
    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> types = new HashSet<>();
        types.add(MvpEntity.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Elements elements, Messager messager, Filer filer, Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {



        Set<? extends Element> entityElements  = roundEnvironment.getElementsAnnotatedWith(MvpEntity.class);

        if(entityElements == null || entityElements.size() == 0){
            return false;
        }


        Element entity = entityElements.iterator().next();

        //检查element类型
            if (entity.getModifiers().contains(Modifier.PRIVATE))
            {
                return false;
            }

            if(entity.getKind() == ElementKind.CLASS){


                TypeElement typeElement = (TypeElement) entity;

                String className = typeElement.getSimpleName().toString(); // HttpResult
                String packageName = elements.getPackageOf(entity).getQualifiedName().toString(); // com.m.k.seetaoism.data.entity

                generateClass(filer,className,packageName);
            }


        return false;
    }


    private void generateClass(Filer filer,String className, String packageName){


        ParameterizedTypeName superClass = ParameterizedTypeName.get(ClassName.get(packageName,className), TypeVariableName.get("D")); // HttpResult<D>

        TypeSpec.Builder builder = TypeSpec.classBuilder("ProxyEntity")
                .addTypeVariable(TypeVariableName.get("D"))
                .addModifiers(Modifier.PUBLIC)
                .superclass(superClass);




        JavaFile file = JavaFile.builder("com.m.k.mvp.data.entity", builder.build()).build();

        try {
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
