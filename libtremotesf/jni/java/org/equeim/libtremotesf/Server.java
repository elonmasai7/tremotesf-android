/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.1.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.equeim.libtremotesf;

public class Server {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected Server(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Server obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtremotesfJNI.delete_Server(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setName(String value) {
    libtremotesfJNI.Server_name_set(swigCPtr, this, value);
  }

  public String getName() {
    return libtremotesfJNI.Server_name_get(swigCPtr, this);
}

  public void setAddress(String value) {
    libtremotesfJNI.Server_address_set(swigCPtr, this, value);
  }

  public String getAddress() {
    return libtremotesfJNI.Server_address_get(swigCPtr, this);
}

  public void setPort(int value) {
    libtremotesfJNI.Server_port_set(swigCPtr, this, value);
  }

  public int getPort() {
    return libtremotesfJNI.Server_port_get(swigCPtr, this);
  }

  public void setApiPath(String value) {
    libtremotesfJNI.Server_apiPath_set(swigCPtr, this, value);
  }

  public String getApiPath() {
    return libtremotesfJNI.Server_apiPath_get(swigCPtr, this);
}

  public void setProxyType(int value) {
    libtremotesfJNI.Server_proxyType_set(swigCPtr, this, value);
  }

  public int getProxyType() {
    return libtremotesfJNI.Server_proxyType_get(swigCPtr, this);
  }

  public void setProxyHostname(String value) {
    libtremotesfJNI.Server_proxyHostname_set(swigCPtr, this, value);
  }

  public String getProxyHostname() {
    return libtremotesfJNI.Server_proxyHostname_get(swigCPtr, this);
}

  public void setProxyPort(int value) {
    libtremotesfJNI.Server_proxyPort_set(swigCPtr, this, value);
  }

  public int getProxyPort() {
    return libtremotesfJNI.Server_proxyPort_get(swigCPtr, this);
  }

  public void setProxyUser(String value) {
    libtremotesfJNI.Server_proxyUser_set(swigCPtr, this, value);
  }

  public String getProxyUser() {
    return libtremotesfJNI.Server_proxyUser_get(swigCPtr, this);
}

  public void setProxyPassword(String value) {
    libtremotesfJNI.Server_proxyPassword_set(swigCPtr, this, value);
  }

  public String getProxyPassword() {
    return libtremotesfJNI.Server_proxyPassword_get(swigCPtr, this);
}

  public void setHttps(boolean value) {
    libtremotesfJNI.Server_https_set(swigCPtr, this, value);
  }

  public boolean getHttps() {
    return libtremotesfJNI.Server_https_get(swigCPtr, this);
  }

  public void setSelfSignedCertificateEnabled(boolean value) {
    libtremotesfJNI.Server_selfSignedCertificateEnabled_set(swigCPtr, this, value);
  }

  public boolean getSelfSignedCertificateEnabled() {
    return libtremotesfJNI.Server_selfSignedCertificateEnabled_get(swigCPtr, this);
  }

  public void setSelfSignedCertificate(byte[] value) {
    libtremotesfJNI.Server_selfSignedCertificate_set(swigCPtr, this, value);
  }

  public byte[] getSelfSignedCertificate() {
    return libtremotesfJNI.Server_selfSignedCertificate_get(swigCPtr, this);
}

  public void setClientCertificateEnabled(boolean value) {
    libtremotesfJNI.Server_clientCertificateEnabled_set(swigCPtr, this, value);
  }

  public boolean getClientCertificateEnabled() {
    return libtremotesfJNI.Server_clientCertificateEnabled_get(swigCPtr, this);
  }

  public void setClientCertificate(byte[] value) {
    libtremotesfJNI.Server_clientCertificate_set(swigCPtr, this, value);
  }

  public byte[] getClientCertificate() {
    return libtremotesfJNI.Server_clientCertificate_get(swigCPtr, this);
}

  public void setAuthentication(boolean value) {
    libtremotesfJNI.Server_authentication_set(swigCPtr, this, value);
  }

  public boolean getAuthentication() {
    return libtremotesfJNI.Server_authentication_get(swigCPtr, this);
  }

  public void setUsername(String value) {
    libtremotesfJNI.Server_username_set(swigCPtr, this, value);
  }

  public String getUsername() {
    return libtremotesfJNI.Server_username_get(swigCPtr, this);
}

  public void setPassword(String value) {
    libtremotesfJNI.Server_password_set(swigCPtr, this, value);
  }

  public String getPassword() {
    return libtremotesfJNI.Server_password_get(swigCPtr, this);
}

  public void setUpdateInterval(int value) {
    libtremotesfJNI.Server_updateInterval_set(swigCPtr, this, value);
  }

  public int getUpdateInterval() {
    return libtremotesfJNI.Server_updateInterval_get(swigCPtr, this);
  }

  public void setBackgroundUpdateInterval(int value) {
    libtremotesfJNI.Server_backgroundUpdateInterval_set(swigCPtr, this, value);
  }

  public int getBackgroundUpdateInterval() {
    return libtremotesfJNI.Server_backgroundUpdateInterval_get(swigCPtr, this);
  }

  public void setTimeout(int value) {
    libtremotesfJNI.Server_timeout_set(swigCPtr, this, value);
  }

  public int getTimeout() {
    return libtremotesfJNI.Server_timeout_get(swigCPtr, this);
  }

  public Server() {
    this(libtremotesfJNI.new_Server(), true);
  }

  public final static class ProxyType {
    public final static int Default = 0;
    public final static int Http = Default + 1;
    public final static int Socks5 = Http + 1;
  }

}
