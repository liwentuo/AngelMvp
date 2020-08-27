package com.example.compiler;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class MvpProcessor extends AbstractProcessor {


    ProcessorUtils mProcessorUtils;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mProcessorUtils = new ProcessorUtils(processingEnvironment.getElementUtils(),processingEnvironment.getMessager(),processingEnvironment.getFiler());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {

       return mProcessorUtils.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        mProcessorUtils.process(set,roundEnvironment);

        return true;

/*

        Set<? extends Element> elements  = roundEnvironment.getElementsAnnotatedWith(MvpEntity.class);

        for(Element element : elements){
            //检查element类型
            if (element.getModifiers().contains(Modifier.PRIVATE))
            {
                return false;
            }
            if(element.getKind() == ElementKind.CLASS){

                TypeElement typeElement = (TypeElement) element;

                String className = typeElement.getSimpleName().toString();
                String packageName = mElementUtils.getPackageOf(element).getQualifiedName().toString();

                generateClass(className,packageName);
            }


        }
*/









/*

        elements = roundEnvironment.getElementsAnnotatedWith(BaseUrl.class);

        Element setElement = elements.iterator().next();

        elements = roundEnvironment.getElementsAnnotatedWith(ApiService.class);

        Element apiElement = elements.iterator().next();



            if (setElement.getModifiers().contains(Modifier.PRIVATE))
            {
                return false;
            }
            if(setElement.getKind() == ElementKind.FIELD){

                VariableElement typeElement = (VariableElement) setElement;

               String baseurl =  typeElement.getConstantValue().toString();


                TypeSpec.Builder config1 = TypeSpec.classBuilder("MvpConfig")
                        .addModifiers(Modifier.PUBLIC);


                FieldSpec baseUrlField = FieldSpec.builder(String.class,"BASE_URL",Modifier.PUBLIC,Modifier.STATIC,Modifier.FINAL)
                        .initializer("$S",baseurl)
                        .build();

                config1.addField(baseUrlField);

                    //ClassName activity = ClassName.get("android.app", "Activity");

                    ClassName context= ClassName.get("android.content","Context");
                    ClassName paramsGetter = ClassName.get("com.m.k.mvp","ParamsGetter");


                    FieldSpec contextSpec = FieldSpec.builder(context,"context",Modifier.PRIVATE).build();
                    FieldSpec paramsGetterSpec = FieldSpec.builder(paramsGetter,"paramsGetter", Modifier.PRIVATE).build();




                    ParameterSpec contextParameterSpec = ParameterSpec.builder(context,"context").build();
                    ParameterSpec parameterParameterSpec = ParameterSpec.builder(paramsGetter,"paramsGetter").build();


                    config1.addField(contextSpec).
                            addField(paramsGetterSpec);

                    MethodSpec constructor1 = MethodSpec.constructorBuilder()
                            .addParameter(parameterParameterSpec)
                            .addParameter(contextParameterSpec)
                            .addModifiers(Modifier.PUBLIC)
                            .addStatement("this.$N = $N",contextSpec,"context")
                            .addStatement("this.$N = $N",paramsGetterSpec,"paramsGetter")
                            .build();
                MethodSpec constructor2 = MethodSpec.constructorBuilder()
                        .addParameter(contextParameterSpec)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.$N = $N",contextSpec,"context")
                        .build();


                    config1.addMethod(constructor1);
                    config1.addMethod(constructor2);

                */
/**
                 *
                 *//*


                MethodSpec getContext = MethodSpec.methodBuilder("getContext").addModifiers(Modifier.PUBLIC)
                        .returns(context)
                        .addStatement("return this.$N",contextSpec)
                        .build();


                MethodSpec getParamsGetter = MethodSpec.methodBuilder("getParamsGetter").addModifiers(Modifier.PUBLIC)
                        .returns(paramsGetter)
                        .addStatement("return this.$N",paramsGetterSpec)
                        .build();
                config1.addMethod(getContext);
                config1.addMethod(getParamsGetter);



                JavaFile file = JavaFile.builder("com.m.k.mvp.config", config1.build()).build();


                try {
                    file.writeTo(mFiler);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }










        elements = roundEnvironment.getElementsAnnotatedWith(ApiService.class);

        for(Element element : elements){
            if (element.getModifiers().contains(Modifier.PRIVATE))
            {
                return false;
            }

            if(element.getKind() == ElementKind.INTERFACE){

                TypeElement typeElement = (TypeElement) element;

                String className = typeElement.getQualifiedName().toString();




            }


        }
*/


    }




}
