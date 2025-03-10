package org.example.common.model;

import lombok.Data;

@Data
public class NetworkConnectionDto {

  Long timestamp;
  String src_ip;
  String src_port;
  String dst_ip;
  String dst_port;
  String domain;

  public String getBlueprint() {
    return src_ip.concat(":")
        .concat(src_port)
        .concat(":")
        .concat(dst_ip)
        .concat(":")
        .concat(dst_port)
        .concat(":")
        .concat(timestamp.toString())
        .concat(":")
        .concat(domain);
  }
}
