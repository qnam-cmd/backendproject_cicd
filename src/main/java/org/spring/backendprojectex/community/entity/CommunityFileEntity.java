package org.spring.backendprojectex.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "community_file_tb06")
public class CommunityFileEntity extends BasicTime {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_file_id")
    private Long id;

    @Column(nullable = false)
    private String newFileName; // 새이름 -> DB, 로컬 저장소 저장이름

    @Column(nullable = false)
    private String oldFileName; // 파일 원래이름

    // N:1
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private CommunityEntity communityEntity;



}
