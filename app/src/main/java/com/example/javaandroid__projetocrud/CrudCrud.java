package com.example.javaandroid__projetocrud;

public class CrudCrud extends Services{

    private String auth;

    protected void setAuth(String auth) {
        System.out.println("AUTH: " + auth);
        this.auth = auth;
    }

    protected Number conexion() {
        if(!this.auth.isEmpty()) {

            return conexion(this.auth);
        }

        return null;
    }

    protected String getMoviesAll() {
        if(!this.auth.isEmpty()){

            return getMoviesAll(this.auth);
        }

        return null;
    }

    protected void newMovie(String name) {
        if(!this.auth.isEmpty()){
            newMovie(this.auth, name);
        }
    }

    protected void updateMovie(String name, String id) {
        if(!this.auth.isEmpty()){
            updateMovie(this.auth, name, id);
        }
    }

    protected void deleteMovie(String id) {
        if(!this.auth.isEmpty()){
            deleteMovie(this.auth, id);
        }
    }

}
