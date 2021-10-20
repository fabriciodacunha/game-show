import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPlay, Play } from '../play.model';
import { PlayService } from '../service/play.service';

@Component({
  selector: 'jhi-play-update',
  templateUrl: './play-update.component.html',
})
export class PlayUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    playData: [],
  });

  constructor(protected playService: PlayService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ play }) => {
      if (play.id === undefined) {
        const today = dayjs().startOf('day');
        play.playData = today;
      }

      this.updateForm(play);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const play = this.createFromForm();
    if (play.id !== undefined) {
      this.subscribeToSaveResponse(this.playService.update(play));
    } else {
      this.subscribeToSaveResponse(this.playService.create(play));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlay>>): void {
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

  protected updateForm(play: IPlay): void {
    this.editForm.patchValue({
      id: play.id,
      playData: play.playData ? play.playData.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IPlay {
    return {
      ...new Play(),
      id: this.editForm.get(['id'])!.value,
      playData: this.editForm.get(['playData'])!.value ? dayjs(this.editForm.get(['playData'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
