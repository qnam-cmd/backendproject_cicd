package org.spring.backendprojectex.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board_file_tb06")
public class BoardFileEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_file_id")
    private Long id;

    @Column(nullable = false)
    private String newFileName; // 새이름 -> DB, 로컬 저장소 저장이름

    @Column(nullable = false)
    private String oldFileName; // 파일 원래이름

    // N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

}
