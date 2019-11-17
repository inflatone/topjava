package ru.javaops.topjava;


public class Profiles {
    public static final String JDBC = "jdbc";
    public static final String JPA = "jpa";
    public static final String DATA_JPA = "datajpa";

    public static final String REPOSITORY_IMPLEMENTATION = DATA_JPA;

    public static final String POSTGRES_DB = "postgres";
    public static final String HSQL_DB = "hsqldb";

    public static String getActiveDbProfile() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            return HSQL_DB;
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("org.postgresql.Driver");
                return POSTGRES_DB;
            } catch (ClassNotFoundException e1) {
                throw new IllegalStateException("Could not find DB driver");
            }
        }
    }
}
