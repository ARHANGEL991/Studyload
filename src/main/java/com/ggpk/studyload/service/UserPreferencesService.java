package com.ggpk.studyload.service;

public interface UserPreferencesService {

    String getTeacherReportFolderPath();

    String getGroupReportFolderPath();

    String getYearReportFolderPath();

    void setTeacherReportPath(String path);

    void setGroupReportPath(String path);

    void setYearReportPath(String path);

    String getTeacherReportTemplateFilePath();

    String getGroupReportTemplateFilePath();

    String getYearReportTemplateFilePath();

    void setTeacherReportTemplateFilePath(String filePath);

    void setGroupReportTemplateFilePath(String filePath);

    void setYearReportTemplateFilePath(String filePath);

    void setImportPath(String path);

    String getImportPath();


}
