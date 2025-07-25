/*
 *  Copyright 2025 yangxj96.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.yangxj96.spectra.core.system.javabean.from;

import com.yangxj96.spectra.common.base.Verify;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 字典数据入参
 * </p>
 *
 * @author Jack Young
 * @version 1.0
 * @since 2025/6/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictDataFrom {

    /**
     * 主键ID
     */
    @NotNull(message = "ID不能为空", groups = Verify.Update.class)
    @Null(message = "新增时不能有ID存在", groups = Verify.Insert.class)
    private Long id;

    /**
     * 字典类型ID
     */
    @NotNull(message = "字典类型不能为空", groups = {Verify.Insert.class, Verify.Update.class})
    private Long gid;

    /**
     * 标签
     */
    @NotNull(message = "标签不能为空", groups = {Verify.Insert.class, Verify.Update.class})
    private String label;

    /**
     * 值
     */
    @NotNull(message = "字典值不能为空", groups = {Verify.Insert.class, Verify.Update.class})
    private String value;

    /**
     * 排序
     */
    private Short sort;

    /**
     * 状态
     */
    @NotNull(message = "字典状态不能为空", groups = {Verify.Insert.class, Verify.Update.class})
    private Short state;

    /**
     * 备注
     */
    private String remark;

}
