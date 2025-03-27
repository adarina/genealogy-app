package com.ada.genealogyapp.tree.dto.params;


import com.ada.genealogyapp.tree.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaveTreeParams extends BaseParams {
    private Tree tree;
}