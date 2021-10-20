jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IQuestao, Questao } from '../questao.model';
import { QuestaoService } from '../service/questao.service';

import { QuestaoRoutingResolveService } from './questao-routing-resolve.service';

describe('Questao routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: QuestaoRoutingResolveService;
  let service: QuestaoService;
  let resultQuestao: IQuestao | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(QuestaoRoutingResolveService);
    service = TestBed.inject(QuestaoService);
    resultQuestao = undefined;
  });

  describe('resolve', () => {
    it('should return IQuestao returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestao = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultQuestao).toEqual({ id: 123 });
    });

    it('should return new IQuestao if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestao = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultQuestao).toEqual(new Questao());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Questao })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestao = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultQuestao).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
