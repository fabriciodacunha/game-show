import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEdicao, Edicao } from '../edicao.model';
import { EdicaoService } from '../service/edicao.service';
import { IPlay } from 'app/entities/play/play.model';
import { PlayService } from 'app/entities/play/service/play.service';

@Component({
  selector: 'jhi-edicao-update',
  templateUrl: './edicao-update.component.html',
})
export class EdicaoUpdateComponent implements OnInit {
  isSaving = false;

  playsCollection: IPlay[] = [];

  editForm = this.fb.group({
    id: [],
    edicaoTitulo: [],
    edicaoData: [],
    play: [],
  });

  constructor(
    protected edicaoService: EdicaoService,
    protected playService: PlayService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ edicao }) => {
      this.updateForm(edicao);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const edicao = this.createFromForm();
    if (edicao.id !== undefined) {
      this.subscribeToSaveResponse(this.edicaoService.update(edicao));
    } else {
      this.subscribeToSaveResponse(this.edicaoService.create(edicao));
    }
  }

  trackPlayById(index: number, item: IPlay): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEdicao>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(edicao: IEdicao): void {
    this.editForm.patchValue({
      id: edicao.id,
      edicaoTitulo: edicao.edicaoTitulo,
      edicaoData: edicao.edicaoData,
      play: edicao.play,
    });

    this.playsCollection = this.playService.addPlayToCollectionIfMissing(this.playsCollection, edicao.play);
  }

  protected loadRelationshipsOptions(): void {
    this.playService
      .query({ filter: 'edicao-is-null' })
      .pipe(map((res: HttpResponse<IPlay[]>) => res.body ?? []))
      .pipe(map((plays: IPlay[]) => this.playService.addPlayToCollectionIfMissing(plays, this.editForm.get('play')!.value)))
      .subscribe((plays: IPlay[]) => (this.playsCollection = plays));
  }

  protected createFromForm(): IEdicao {
    return {
      ...new Edicao(),
      id: this.editForm.get(['id'])!.value,
      edicaoTitulo: this.editForm.get(['edicaoTitulo'])!.value,
      edicaoData: this.editForm.get(['edicaoData'])!.value,
      play: this.editForm.get(['play'])!.value,
    };
  }
}
