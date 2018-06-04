package com.ggpk.studyload.service;

public interface UserPreferencesService {

    String getTeacherReportPath();

    String getGroupReportPath();

    void setTeacherReportPath(String path);

    void setGroupReportPath(String path);

    String getTeacherReportTemplateFilePath();

    String getGroupReportTemplateFilePath();

    void setTeacherReportTemplateFilePath(String filePath);

    void setGroupReportTemplateFilePath(String filePath);

    void setImportPath(String path);

    String getImportPath();


}
