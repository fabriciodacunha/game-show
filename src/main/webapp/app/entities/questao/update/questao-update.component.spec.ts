jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuestaoService } from '../service/questao.service';
import { IQuestao, Questao } from '../questao.model';
import { IEdicao } from 'app/entities/edicao/edicao.model';
import { EdicaoService } from 'app/entities/edicao/service/edicao.service';

import { QuestaoUpdateComponent } from './questao-update.component';

describe('Questao Management Update Component', () => {
  let comp: QuestaoUpdateComponent;
  let fixture: ComponentFixture<QuestaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questaoService: QuestaoService;
  let edicaoService: EdicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [QuestaoUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(QuestaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questaoService = TestBed.inject(QuestaoService);
    edicaoService = TestBed.inject(EdicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Edicao query and add missing value', () => {
      const questao: IQuestao = { id: 456 };
      const edicao: IEdicao = { id: 2277 };
      questao.edicao = edicao;

      const edicaoCollection: IEdicao[] = [{ id: 30482 }];
      jest.spyOn(edicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: edicaoCollection })));
      const additionalEdicaos = [edicao];
      const expectedCollection: IEdicao[] = [...additionalEdicaos, ...edicaoCollection];
      jest.spyOn(edicaoService, 'addEdicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questao });
      comp.ngOnInit();

      expect(edicaoService.query).toHaveBeenCalled();
      expect(edicaoService.addEdicaoToCollectionIfMissing).toHaveBeenCalledWith(edicaoCollection, ...additionalEdicaos);
      expect(comp.edicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const questao: IQuestao = { id: 456 };
      const edicao: IEdicao = { id: 59859 };
      questao.edicao = edicao;

      activatedRoute.data = of({ questao });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(questao));
      expect(comp.edicaosSharedCollection).toContain(edicao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Questao>>();
      const questao = { id: 123 };
      jest.spyOn(questaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questao }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(questaoService.update).toHaveBeenCalledWith(questao);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Questao>>();
      const questao = new Questao();
      jest.spyOn(questaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questao }));
      saveSubject.complete();

      // THEN
      expect(questaoService.create).toHaveBeenCalledWith(questao);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Questao>>();
      const questao = { id: 123 };
      jest.spyOn(questaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questaoService.update).toHaveBeenCalledWith(questao);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEdicaoById', () => {
      it('Should return tracked Edicao primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEdicaoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
