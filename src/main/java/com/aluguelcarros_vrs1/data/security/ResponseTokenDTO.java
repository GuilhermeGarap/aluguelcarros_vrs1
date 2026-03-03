package com.aluguelcarros_vrs1.data.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ResponseTokenDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String userName;
    private Boolean animated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;
    private List<String> roles;

    public ResponseTokenDTO() {}

    public ResponseTokenDTO(String userName, Boolean authenticated, Date created, Date expiration,
                            String accessToken, String refreshToken, List<String> roles) {
        this.userName = userName;
        this.animated = authenticated;
        this.created = created;
        this.expiration = expiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

    // Getters e Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Boolean getAuthenticated() { return animated; }
    public void setAuthenticated(Boolean authenticated) { this.animated = authenticated; }

    public Date getCreated() { return created; }
    public void setCreated(Date created) { this.created = created; }

    public Date getExpiration() { return expiration; }
    public void setExpiration(Date expiration) { this.expiration = expiration; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseTokenDTO responseTokenDTO = (ResponseTokenDTO) o;
        return Objects.equals(userName, responseTokenDTO.userName) &&
                Objects.equals(animated, responseTokenDTO.animated) &&
                Objects.equals(roles, responseTokenDTO.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, animated, roles);
    }
}