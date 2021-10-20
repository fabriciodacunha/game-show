jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EdicaoService } from '../service/edicao.service';
import { IEdicao, Edicao } from '../edicao.model';
import { IPlay } from 'app/entities/play/play.model';
import { PlayService } from 'app/entities/play/service/play.service';

import { EdicaoUpdateComponent } from './edicao-update.component';

describe('Edicao Management Update Component', () => {
  let comp: EdicaoUpdateComponent;
  let fixture: ComponentFixture<EdicaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let edicaoService: EdicaoService;
  let playService: PlayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EdicaoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EdicaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EdicaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    edicaoService = TestBed.inject(EdicaoService);
    playService = TestBed.inject(PlayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call play query and add missing value', () => {
      const edicao: IEdicao = { id: 456 };
      const play: IPlay = { id: 96190 };
      edicao.play = play;

      const playCollection: IPlay[] = [{ id: 73356 }];
      jest.spyOn(playService, 'query').mockReturnValue(of(new HttpResponse({ body: playCollection })));
      const expectedCollection: IPlay[] = [play, ...playCollection];
      jest.spyOn(playService, 'addPlayToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      expect(playService.query).toHaveBeenCalled();
      expect(playService.addPlayToCollectionIfMissing).toHaveBeenCalledWith(playCollection, play);
      expect(comp.playsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const edicao: IEdicao = { id: 456 };
      const play: IPlay = { id: 32002 };
      edicao.play = play;

      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(edicao));
      expect(comp.playsCollection).toContain(play);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Edicao>>();
      const edicao = { id: 123 };
      jest.spyOn(edicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: edicao }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(edicaoService.update).toHaveBeenCalledWith(edicao);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Edicao>>();
      const edicao = new Edicao();
      jest.spyOn(edicaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: edicao }));
      saveSubject.complete();

      // THEN
      expect(edicaoService.create).toHaveBeenCalledWith(edicao);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Edicao>>();
      const edicao = { id: 123 };
      jest.spyOn(edicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(edicaoService.update).toHaveBeenCalledWith(edicao);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlayById', () => {
      it('Should return tracked Play primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlayById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
