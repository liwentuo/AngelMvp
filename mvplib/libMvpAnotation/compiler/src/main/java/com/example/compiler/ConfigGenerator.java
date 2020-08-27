package com.example.compiler;

import com.m.k.anotaion.ApiService;
import com.m.k.anotaion.BaseUrl;
import com.m.k.anotaion.GsonConverter;
import com.m.k.anotaion.OkInterceptor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class ConfigGenerator implements FileGenerator {

    /**
     * mvpConfigPackageName = com.m.k.mvp.config
     * mvpConfigClassName = MvpConfig
     * <p>
     * <p>
     * baseUrl = BASE_URL
     * appApiService = APP_API_SERVICE
     * appGSONConverter = APP_GSON_CONVERTER
     * appOkInterceptorPrefix = APP_OK_INTERCEPTOR_
     * <p>
     * <p>
     * mvpDataServicePackageName=com.m.k.mvp.data.ok
     * mvpDataServiceClassName= MvpDataService
     * <p>
     * <p>
     * mvpApiServicePackageName=com.m.k.mvp.data.ok
     * mvpApiServiceClassName=MvpApiService
     */
    private static final String PROPERTIES_FILE_NAME = "./mvplib/mvp.properties";

    private static final String PROPERTIES_KEY_MVP_DATA_SERVICE_PK_NAME = "mvpDataServicePackageName";
    private static final String PROPERTIES_KEY_MVP_DATA_SERVICE_C_NAME = "mvpDataServiceClassName";

    private static final String PROPERTIES_KEY_MVP_API_SERVICE_PK_NAME = "mvpApiServicePackageName";
    private static final String PROPERTIES_KEY_MVP_API_SERVICE_C_NAME = "mvpApiServiceClassName";


    private static final String PROPERTIES_KEY_MVP_CONFIG_PK_NAME = "mvpConfigPackageName";
    private static final String PROPERTIES_KEY_MVP_CONFIG_C_NAME = "mvpConfigClassName";

    private static final String PROPERTIES_KEY_BASE_URL_FIELD_NAME = "baseUrlFieldName";
    private static final String PROPERTIES_KEY_APP_API_SERVICE_FIELD_NAME = "appApiServiceFieldName";
    private static final String PROPERTIES_KEY_APP_GSON_CONVERTER_FIELD_NAME = "appGSONConverterFieldName";
    private static final String PROPERTIES_KEY_APP_OK_INTERCEPT0R_PREFIX_FIELD_NAME = "appOkInterceptorPrefixFieldName";


    private static final String PROPERTIES_KEY_GET_MVP_SERVICE_METHOD_NAME = "mvpDataServiceGetMvpServiceMethodName";
    private static final String PROPERTIES_KEY_GET_APP_SERVICE_METHOD_NAME = "mvpDataServiceGetAppServiceMethodName";


    private String mvpDataServicePackageName;
    private String mvpDataServiceClassName;

    private String mvpApiServicePackageName;
    private String mvpApiServiceClassName;

    private String mvpConfigClassName;
    private String mvpConfigPackageName;

    private String baseUrlFieldName;
    private String appApiServiceFieldName;
    private String appAppGsonConverterFieldName;
    private String appAppInterceptorFieldName;

    private String getAppApiServiceName;
    private String getMvpApiServiceName;


    public ConfigGenerator() {

        Properties properties = new Properties();
        try {

            properties.load(new FileInputStream(new File(PROPERTIES_FILE_NAME)));

            mvpDataServicePackageName = properties.getProperty(PROPERTIES_KEY_MVP_DATA_SERVICE_PK_NAME);
            mvpDataServiceClassName = properties.getProperty(PROPERTIES_KEY_MVP_DATA_SERVICE_C_NAME);

            mvpApiServicePackageName = properties.getProperty(PROPERTIES_KEY_MVP_API_SERVICE_PK_NAME);
            mvpApiServiceClassName = properties.getProperty(PROPERTIES_KEY_MVP_API_SERVICE_C_NAME);

            mvpConfigPackageName = properties.getProperty(PROPERTIES_KEY_MVP_CONFIG_PK_NAME);
            mvpConfigClassName = properties.getProperty(PROPERTIES_KEY_MVP_CONFIG_C_NAME);

            baseUrlFieldName = properties.getProperty(PROPERTIES_KEY_BASE_URL_FIELD_NAME);
            appApiServiceFieldName = properties.getProperty(PROPERTIES_KEY_APP_API_SERVICE_FIELD_NAME);
            appAppGsonConverterFieldName = properties.getProperty(PROPERTIES_KEY_APP_GSON_CONVERTER_FIELD_NAME);
            appAppInterceptorFieldName = properties.getProperty(PROPERTIES_KEY_APP_OK_INTERCEPT0R_PREFIX_FIELD_NAME);

            getAppApiServiceName = properties.getProperty(PROPERTIES_KEY_GET_APP_SERVICE_METHOD_NAME);
            getMvpApiServiceName = properties.getProperty(PROPERTIES_KEY_GET_MVP_SERVICE_METHOD_NAME);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BaseUrl.class.getCanonicalName());
        types.add(ApiService.class.getCanonicalName());
        types.add(GsonConverter.class.getCanonicalName());
        types.add(OkInterceptor.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Elements elements, Messager messager, Filer filer, Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> baseUrlElements = roundEnvironment.getElementsAnnotatedWith(BaseUrl.class);
        Set<? extends Element> ApiServiceElements = roundEnvironment.getElementsAnnotatedWith(ApiService.class);
        Set<? extends Element> converterElements = roundEnvironment.getElementsAnnotatedWith(GsonConverter.class);

        Set<? extends Element> interceptorElements = roundEnvironment.getElementsAnnotatedWith(OkInterceptor.class);


        Element baseUrl = null;

        Element apiService = null;

        Element converter = null;


        if (baseUrlElements != null && baseUrlElements.size() > 0) {
            baseUrl = baseUrlElements.iterator().next();
        }

        if (ApiServiceElements != null && ApiServiceElements.size() > 0) {


            apiService = ApiServiceElements.iterator().next();

        }


        if (converterElements != null && converterElements.size() > 0) {
            converter = converterElements.iterator().next();
        }


        if (!isValidBaseUrlElement(baseUrl) && !isValidApiServiceElement(apiService) && !isValidConverterElement(converter)) {
            return false;
        }


        TypeSpec.Builder config = TypeSpec.classBuilder(mvpConfigClassName)
                .addModifiers(Modifier.PUBLIC);


        if (isValidBaseUrlElement(baseUrl)) {

            VariableElement variableElement = (VariableElement) baseUrl;

            String urlValue = variableElement.getConstantValue().toString(); // http:www.xxx.com


            FieldSpec baseUrlField = FieldSpec.builder(String.class, baseUrlFieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$S", urlValue)
                    .build();

            config.addField(baseUrlField);
        }

        if (isValidApiServiceElement(apiService)) {

            TypeElement typeElement = (TypeElement) apiService;

            ClassName name = ClassName.get(elements.getPackageOf(apiService).toString(), typeElement.getSimpleName().toString());

            ClassName name1 = ClassName.get("java.lang", "Class");

            ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(name1, name);

            FieldSpec baseUrlField = FieldSpec.builder(parameterizedTypeName, appApiServiceFieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$N.class", typeElement.getSimpleName())
                    .build();
            config.addField(baseUrlField);


            generatorDataService(filer, name);


        }

        if (isValidConverterElement(converter)) {


            TypeElement typeElement = (TypeElement) converter;


            String className = typeElement.getQualifiedName().toString();

            FieldSpec converterField = FieldSpec.builder(String.class, appAppGsonConverterFieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$S", className)
                    .build();

            config.addField(converterField);
        }


        for (Element interceptor : interceptorElements) {

            if (isValidInterceptorElement(interceptor)) {

                TypeElement typeElement = (TypeElement) interceptor;

                AnnotationMirror annotationMirror = typeElement.getAnnotationMirrors().get(0);

                Integer order = (Integer) annotationMirror.getElementValues().values().iterator().next().getValue();


                String className = typeElement.getQualifiedName().toString();

                FieldSpec interceptorField = FieldSpec.builder(String.class, appAppInterceptorFieldName + order, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", className)
                        .build();

                config.addField(interceptorField);
            }

        }


        JavaFile file = JavaFile.builder(mvpConfigPackageName, config.build()).build();


        try {
            file.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }


    private void generatorDataService(Filer filer, ClassName appApiServiceClassName) {

        ClassName mvpDataService = ClassName.get(mvpDataServicePackageName, mvpDataServiceClassName);

        MethodSpec methodSpec1 = MethodSpec.methodBuilder(getAppApiServiceName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(appApiServiceClassName)
                .addStatement("return ($N)$N.$N()", appApiServiceClassName.simpleName(), mvpDataService.simpleName(), getAppApiServiceName).build();

        ClassName mvpApiService = ClassName.get(mvpApiServicePackageName, mvpApiServiceClassName);

        MethodSpec methodSpec2 = MethodSpec.methodBuilder(getMvpApiServiceName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(mvpApiService)
                .addStatement("return $N.$N()", mvpDataService.simpleName(), getMvpApiServiceName).build();

        TypeSpec typeSpec = TypeSpec.classBuilder("AppDataService")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec1)
                .addMethod(methodSpec2)
                .build();


        JavaFile javaFile = JavaFile.builder(mvpDataService.packageName(), typeSpec).build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private boolean isValidBaseUrlElement(Element element) {
        if (element == null) {
            return false;
        }

        if (element.getKind() != ElementKind.FIELD) {
            return false;
        }

        ArrayList<Modifier> modifies = new ArrayList<>();
        modifies.add(Modifier.PUBLIC);
        modifies.add(Modifier.STATIC);
        modifies.add(Modifier.FINAL);

        if (!element.getModifiers().containsAll(modifies)) {
            return false;
        }

        return true;
    }

    private boolean isValidApiServiceElement(Element element) {
        if (element == null) {
            return false;
        }

        if (element.getKind() != ElementKind.INTERFACE) {
            return false;
        }

        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            return false;
        }

        return true;
    }

    private boolean isValidConverterElement(Element element) {
        if (element == null) {
            return false;
        }

        if (element.getKind() != ElementKind.CLASS) {
            return false;
        }

        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            return false;
        }

        return true;
    }


    private boolean isValidInterceptorElement(Element element) {
        if (element == null) {
            return false;
        }

        if (element.getKind() != ElementKind.CLASS) {
            return false;
        }

        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            return false;
        }

        return true;
    }
}
