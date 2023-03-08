package com.tju.bclab.vote_backend.io.aelf.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import  com.tju.bclab.vote_backend.io.aelf.schemas.MerklePathNodeDto;

import java.util.List;

public class MerklePathDto {

  @JsonProperty("MerklePathNodes")
  private List<MerklePathNodeDto> merklePathNodes;

  public List<MerklePathNodeDto> getMerklePathNodes() {
    return merklePathNodes;
  }

  public void setMerklePathNodes(List<MerklePathNodeDto> merklePathNodes) {
    this.merklePathNodes = merklePathNodes;
  }
}
