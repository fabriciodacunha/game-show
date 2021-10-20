import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestao, Questao } from '../questao.model';
import { QuestaoService } from '../service/questao.service';
import { IEdicao } from 'app/entities/edicao/edicao.model';
import { EdicaoService } from 'app/entities/edicao/service/edicao.service';
import { Assunto } from 'app/entities/enumerations/assunto.model';
import { Alternativa } from 'app/entities/enumerations/alternativa.model';

@Component({
  selector: 'jhi-questao-update',
  templateUrl: './questao-update.component.html',
})
export class QuestaoUpdateComponent implements OnInit {
  isSaving = false;
  assuntoValues = Object.keys(Assunto);
  alternativaValues = Object.keys(Alternativa);

  edicaosSharedCollection: IEdicao[] = [];

  editForm = this.fb.group({
    id: [],
    questaoTitulo: [],
    alternativaA: [],
    alternativaB: [],
    alternativaC: [],
    alternativaD: [],
    nivelIdade: [],
    assunto: [],
    alternativaCerta: [],
    edicao: [],
  });

  constructor(
    protected questaoService: QuestaoService,
    protected edicaoService: EdicaoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questao }) => {
      this.updateForm(questao);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questao = this.createFromForm();
    if (questao.id !== undefined) {
      this.subscribeToSaveResponse(this.questaoService.update(questao));
    } else {
      this.subscribeToSaveResponse(this.questaoService.create(questao));
    }
  }

  trackEdicaoById(index: number, item: IEdicao): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestao>>): void {
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

  protected updateForm(questao: IQuestao): void {
    this.editForm.patchValue({
      id: questao.id,
      questaoTitulo: questao.questaoTitulo,
      alternativaA: questao.alternativaA,
      alternativaB: questao.alternativaB,
      alternativaC: questao.alternativaC,
      alternativaD: questao.alternativaD,
      nivelIdade: questao.nivelIdade,
      assunto: questao.assunto,
      alternativaCerta: questao.alternativaCerta,
      edicao: questao.edicao,
    });

    this.edicaosSharedCollection = this.edicaoService.addEdicaoToCollectionIfMissing(this.edicaosSharedCollection, questao.edicao);
  }

  protected loadRelationshipsOptions(): void {
    this.edicaoService
      .query()
      .pipe(map((res: HttpResponse<IEdicao[]>) => res.body ?? []))
      .pipe(map((edicaos: IEdicao[]) => this.edicaoService.addEdicaoToCollectionIfMissing(edicaos, this.editForm.get('edicao')!.value)))
      .subscribe((edicaos: IEdicao[]) => (this.edicaosSharedCollection = edicaos));
  }

  protected createFromForm(): IQuestao {
    return {
      ...new Questao(),
      id: this.editForm.get(['id'])!.value,
      questaoTitulo: this.editForm.get(['questaoTitulo'])!.value,
      alternativaA: this.editForm.get(['alternativaA'])!.value,
      alternativaB: this.editForm.get(['alternativaB'])!.value,
      alternativaC: this.editForm.get(['alternativaC'])!.value,
      alternativaD: this.editForm.get(['alternativaD'])!.value,
      nivelIdade: this.editForm.get(['nivelIdade'])!.value,
      assunto: this.editForm.get(['assunto'])!.value,
      alternativaCerta: this.editForm.get(['alternativaCerta'])!.value,
      edicao: this.editForm.get(['edicao'])!.value,
    };
  }
}
