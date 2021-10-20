import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEdicao } from '../edicao.model';

@Component({
  selector: 'jhi-edicao-detail',
  templateUrl: './edicao-detail.component.html',
})
export class EdicaoDetailComponent implements OnInit {
  edicao: IEdicao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ edicao }) => {
      this.edicao = edicao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
