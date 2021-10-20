import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestao } from '../questao.model';

@Component({
  selector: 'jhi-questao-detail',
  templateUrl: './questao-detail.component.html',
})
export class QuestaoDetailComponent implements OnInit {
  questao: IQuestao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questao }) => {
      this.questao = questao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
