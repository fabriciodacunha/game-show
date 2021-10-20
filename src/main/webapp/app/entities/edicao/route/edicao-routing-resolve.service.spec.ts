jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEdicao, Edicao } from '../edicao.model';
import { EdicaoService } from '../service/edicao.service';

import { EdicaoRoutingResolveService } from './edicao-routing-resolve.service';

describe('Edicao routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EdicaoRoutingResolveService;
  let service: EdicaoService;
  let resultEdicao: IEdicao | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(EdicaoRoutingResolveService);
    service = TestBed.inject(EdicaoService);
    resultEdicao = undefined;
  });

  describe('resolve', () => {
    it('should return IEdicao returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEdicao = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEdicao).toEqual({ id: 123 });
    });

    it('should return new IEdicao if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEdicao = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEdicao).toEqual(new Edicao());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Edicao })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEdicao = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEdicao).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
